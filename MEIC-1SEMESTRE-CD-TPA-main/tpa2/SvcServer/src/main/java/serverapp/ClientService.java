package serverapp;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.rabbitmq.client.Channel;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverapp.data.ImageState;
import serverapp.data.SvcImageStatus;
import serverapp.data.SvcSpreadMessage;
import spread.SpreadException;
import svcstubs.ImageBlock;
import svcstubs.ImageData;
import svcstubs.ImageId;
import svcstubs.SvcServiceGrpc;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;



public class ClientService extends SvcServiceGrpc.SvcServiceImplBase {
    //Path of the shared file space
    private final String fileDir;
    private static final Logger logger = LogManager.getLogger(ClientService.class.getName());
    private final GroupMember member;
    private final Gson gson;
    private final Channel channel;
    private final SpreadGroupManager manager;


    public ClientService(String dir, GroupMember member, Gson gson, Channel channel, SpreadGroupManager manager) {
        fileDir = dir;
        this.member = member;
        this.gson = gson;
        this.channel = channel;
        this.manager = manager;
    }
    @Override
    public StreamObserver<ImageBlock> uploadImage(StreamObserver<ImageId> responseObserver) {

        logger.info("Initiated upload Image");
        return new StreamObserver<ImageBlock>() {

            FileOutputStream writer;
            ImageData imageData;
            File image;
            String imageId;

            public String getStorageName(){
                String[] imageName = imageData.getFilename().split("\\.");
                logger.info("Split given name in" + Arrays.toString(imageName));
                String extension = imageName[imageName.length - 1];
                //Rejeitar extensões que não façam parte das selecionadas
                if(!extension.equals("png") && !extension.equals("jpeg") && !extension.equals("jpg"))
                    //De forma a não terminar os processos aka fail gracefully
                    responseObserver.onError(
                            new StatusException(
                                    Status.INVALID_ARGUMENT.withDescription("Image format not supported (options: png,jpeg,jpg)")
                            )
                    );
                String fileName = "";
                //Construir o fileName sem a extensão
                for (int i = 0; i < imageName.length - 1; i++) {
                    fileName += imageName[i];
                }
                return fileName +"-" + UUID.randomUUID() + "." + extension;
            }

            public void deleteImage(){
                if (image.exists()) {
                    logger.error("Image exists, deleting");
                    if (image.delete()){
                        logger.info("Image deleted");
                    }
                    else {
                        logger.error("Failed to delete image");
                    }
                }
            }

            @Override
            public void onNext(ImageBlock imageBlock) {
                if (imageBlock.getOptionsCase() == ImageBlock.OptionsCase.DATA) {
                    imageData = imageBlock.getData();
                    List<String> imageTags = imageData.getTagsList();
                    imageId = getStorageName();
                    //Verificar tags
                    for (String imageTag : imageTags) {
                        logger.info(imageTag);
                    }
                    try {
                        image = new File(fileDir, imageId);
                        logger.info("Writing image to " + image.getAbsolutePath());
                        writer = new FileOutputStream(image);
                    } catch (FileNotFoundException e) {
                        logger.error("File not found: {}", fileDir);
                        throw new RuntimeException(e);
                    }
                    logger.info("Saving image with name: "+ imageId);
                    SvcImageStatus imageStatus = new SvcImageStatus();
                    imageStatus.setOriginalImageName(imageId);
                    imageStatus.setKeywords(imageTags);
                    imageStatus.setImageState(ImageState.UPLOADING);
                    SvcSpreadMessage spreadMessage = new SvcSpreadMessage();
                    spreadMessage.setImageStatus(imageStatus);
                    String json = gson.toJson(spreadMessage);
                    try {
                        //Informar que imagem está a ser uploaded
                        member.SendMessage(json);
                    } catch (SpreadException e) {
                        deleteImage();
                        responseObserver.onError(new StatusException(
                                Status.INTERNAL.withDescription(e.getMessage())));
                        throw new RuntimeException(e);
                    }


                }else {
                    try {
                        writer.write(imageBlock.getBlock().toByteArray());
                    } catch (IOException e) {
                        logger.error("IO exception: " + fileDir);
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                deleteImage();
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                try {
                    writer.close();
                    ImageId id = ImageId.newBuilder().setId(imageId).build();
                    responseObserver.onNext(id);
                    responseObserver.onCompleted();
                } catch (IOException e) {
                    logger.error("IO exception: " + fileDir);
                    throw new RuntimeException(e);
                }
                //Problema de concurrência
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info("Feching image with name: "+ imageId);
                SvcImageStatus imageStatus = manager.getImageStatus(imageId);
                imageStatus.setImageState(ImageState.TO_BE_TAGGED);
                String json = gson.toJson(imageStatus);
                try {
                    //Informar que imagem está para ser tagged
                    member.SendMessage(json);
                } catch (SpreadException e) {
                    deleteImage();
                    responseObserver.onError(new StatusException(
                            Status.INTERNAL.withDescription(e.getMessage())));
                    throw new RuntimeException(e);
                }
                // Nome do exchange em formacto fanout
                String exchangeName = "test_exchange";
                // Publicar message no exchange que envia para queue serializada
                try {
                    channel.basicPublish(exchangeName, "", null, json.getBytes("UTF-8"));
                } catch (IOException e) {
                    deleteImage();
                    responseObserver.onError(new StatusException(
                            Status.INTERNAL.withDescription(e.getMessage())));
                    throw new RuntimeException(e);
                }

            }
        };
    }

    @Override
    public void downloadImage(ImageId request, StreamObserver<ImageBlock> responseObserver) {

        String imageName = request.getId();
        SvcImageStatus imageStatus = manager.getImageStatus(imageName);
        if (imageStatus == null) {
            logger.info("Image Id not found: " + imageName);
            responseObserver.onError(
                    new StatusException(
                            Status.INVALID_ARGUMENT.withDescription("Image not found: " + imageName)
                    )
            );
            return;
        }
        if (!imageStatus.isImageTagged()) {
            logger.info("Image yet not tagged: " + imageName);
            responseObserver.onError(
                    new StatusException(
                            Status.UNAVAILABLE.withDescription("Image not yet tagged: " + imageName)
                    )
            );
            return;
        }
        String markedImageName = imageStatus.getMarkedImageName();
        logger.info("Downloading image with name: " + markedImageName);
        File image = new File(fileDir, markedImageName);
        logger.info("Does image exist? " + image.exists());
        if (!image.exists()) {
            logger.info("Image not found: " + markedImageName);
            responseObserver.onError(
                    new StatusException(
                            Status.INVALID_ARGUMENT.withDescription("Image not found: " + markedImageName)
                    )
            );
        }else {
            logger.info("Initiating image send from path:" + image.getAbsolutePath());
            byte[] buffer = new byte[1024];
            try (InputStream input = new FileInputStream(image)) {
                while (input.read(buffer) >= 0) {
                    try {
                        ByteString bytes = ByteString.copyFrom(buffer);
                        responseObserver.onNext(ImageBlock.newBuilder().setBlock(bytes).build());
                    } catch (Exception ex) {
                        logger.error("Error while sending image");
                        responseObserver.onError(ex);
                    }
                }
                logger.info("Image sent");

            } catch (IOException e) {
                logger.error("IO exception: " + imageName);
                throw new RuntimeException(e);
            }
            responseObserver.onCompleted();
        }
    }

}