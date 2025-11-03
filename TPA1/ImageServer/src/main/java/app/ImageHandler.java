package app;




import clientToImageServiceStubs.ClientToImageserverGrpc;
import clientToImageServiceStubs.Image;
import clientToImageServiceStubs.ImgID;
import clientToImageServiceStubs.StatusInfo;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import io.grpc.stub.StreamObserver;


import org.checkerframework.checker.units.qual.A;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.InetAddress;
import java.util.*;


public class  ImageHandler extends ClientToImageserverGrpc.ClientToImageserverImplBase{


    private final Jedis redis;
    //private final String imagePath = "/var/localimages/";
    // teste windows
    private final String imagePath = "C:\\Users\\marce\\AppData\\Local\\Docker\\wsl\\";
    private  String redishost = null;
    private  int redisPort = 0;
    private  int serverPort = 0;


    public ImageHandler(Jedis redis, String redishost, int redisPort, int serverPort) {
        this.redis = redis;
        this.redishost = redishost;
        this.redisPort = redisPort;
        this.serverPort = serverPort;
    }
    @Override
    public StreamObserver<Image> imageProcessing(StreamObserver<ImgID> responseObserver) {

        return new StreamObserver<Image>() {

            final String imgID = UUID.randomUUID().toString();;
            String filename = null;
            FileOutputStream OutFile = null;
            File TempFile = null;
            //long blocks = 0;

            @Override
            public void onNext(Image image) {
                try {
                    if (image.hasData()) {
                        filename = image.getData().getFilename();
                        TempFile = new File(imagePath + imgID + "_" + filename);
                        OutFile = new FileOutputStream(TempFile);

                        System.out.println("Start receiving image: " + filename);

                    } else if (image.hasBlock() && OutFile != null) {
                        OutFile.write(image.getBlock().toByteArray());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {

                System.err.println("Error on upload " + throwable.getMessage());

            }

            @Override
            public void onCompleted() {
                try {
                    if (OutFile != null) OutFile.close();

                    // get actually ip
                    String serverip = InetAddress.getLocalHost().getHostAddress();

                    // update redis
                    redis.hset("IMG:" + imgID, "status", "PENDING");
                    redis.hset("IMG:" + imgID, "filename", filename);
                    redis.hset("IMG:" + imgID, "input_path", TempFile.getAbsolutePath());
                    redis.hset("IMG:" + imgID, "host", serverip);
                    redis.hset("IMG:" + imgID, "port", String.valueOf(serverPort));

                    System.out.println("Image saved in redis. Start resize container......");

                    // Linux args example: unix:///var/run/docker.sock 8050 7500 /usr/local/servicerest/  /usr/datafiles servicerest21
                    // Windows args example: tcp://localhost:2375 ctrest 8050 7500 d:\share-docker /usr/datafiles servicerest21

                    String containerInputPath = "/usr/datafiles/" + imgID + "_" + filename;
                    redis.hset("IMG:" + imgID, "input_path", containerInputPath);

                    String[] dockerArgs = {
                            //"unix:///var/run/docker.sock",
                            "tcp://localhost:2375",
                            "worker_" + imgID,
                            "8050", "7050",
                            //imagePath+"/", "/app/data/ges/",
                            imagePath, "/usr/datafiles",
                            "m4rcelo0571/worker-resize:v2",
                    };
                    //DockerAPI.main(dockerArgs);

                    // TESTE LOCALMENTE
                    DockerClient docker = DockerClientBuilder.getInstance("tcp://localhost:2375").build();

                    HostConfig hostConfig = new HostConfig()
                            .withBinds(new Bind(imagePath, new Volume("/usr/datafiles")));

                    CreateContainerResponse container = docker.createContainerCmd("m4rcelo0571/worker-resize:v2")
                            .withName("worker_"+ imgID)
                            .withHostConfig(hostConfig)
                            .withCmd(imgID, "host.docker.internal", "6379")
                            .exec();

                    docker.startContainerCmd(container.getId()).exec();
                    System.out.println("Container worker launched with ID: " + container.getId());



                    // deliver ID to client
                    ImgID response =
                            ImgID.newBuilder()
                                    .setId(imgID)
                                    .build();

                    responseObserver.onNext(response);
                    System.out.println("Upload has been completed. Worker running for ID: " + imgID);

                    responseObserver.onCompleted();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
    @Override
    public void downloadImage(ImgID request, StreamObserver<Image> responseObserver) {

        String imgID = request.getId();

        String OutPath = redis.hget("IMG:" + imgID, "output_path");
        String status = redis.hget("IMG:" + imgID, "status");

        //System.out.println("Request download of image ID: " + imgID);

        //long len = redis.llen("image:" + imgID + ":blocks");

        if (OutPath == null || !"DONE".equals(status)) {
            System.err.println("Image not found: " + imgID);
            responseObserver.onError(new Exception("Image not found or not upload yet."));
            return;
        }
        if (OutPath.startsWith("/usr/datafiles")) {
            OutPath = OutPath.replace("/usr/datafiles", imagePath);
        }

        try (FileInputStream fis = new FileInputStream(OutPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                Image imageBlock = Image.newBuilder()
                        .setBlock(com.google.protobuf.ByteString.copyFrom(buffer, 0, bytesRead))
                        .build();
                responseObserver.onNext(imageBlock);
            }
            responseObserver.onCompleted();
            System.out.println("Image successfully: " + OutPath);
        } catch (IOException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    @Override
    public void consultStatus(ImgID request, StreamObserver<StatusInfo> responseObserver) {
        String ImgID = request.getId();
        String Key = "IMG:" + ImgID;

        try {
            if (!redis.exists(Key)) {
                System.out.println("Not found status: " + ImgID);
                responseObserver.onError(
                        new Exception("ID not found in Redis: " + ImgID)
                );
                return;
            }
        Map<String, String> fields = redis.hgetAll(Key);

        StatusInfo info = StatusInfo.newBuilder()
                .setId(fields.getOrDefault("status", "UNKNOWN"))
                .setStatus(fields.getOrDefault("filename", ""))
                .setFilename(fields.getOrDefault("input_path", ""))
                .setInputPath(fields.getOrDefault("output_path", ""))
                .setOutputPath(fields.getOrDefault("host", ""))
                .setHost(fields.getOrDefault("host", ""))
                .setPort(fields.getOrDefault("port", ""))
                .build();

        responseObserver.onNext(info);
        responseObserver.onCompleted();

        System.out.println("Status success for " + ImgID + "-" + info.getStatus());
    } catch (Exception error) {
            error.printStackTrace();
            responseObserver.onError(error);
        }
    }

}


