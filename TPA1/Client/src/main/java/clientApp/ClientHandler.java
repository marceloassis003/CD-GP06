package clientApp;

import clientToImageServiceStubs.*;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class ClientHandler {

    private final ManagedChannel channel;
    private final ClientToImageserverGrpc.ClientToImageserverStub asyncStub;
    private final ClientToImageserverGrpc.ClientToImageserverBlockingStub blockingStub;

    public ClientHandler(String ip, int port) {
        this.channel = ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext()
                .build();

        this.asyncStub = ClientToImageserverGrpc.newStub(channel);
        this.blockingStub = ClientToImageserverGrpc.newBlockingStub(channel);
    }

    public void uploadImage() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Path of the image to upload: ");
            String filePath = scanner.nextLine();

            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

            StreamObserver<ImgID> responseObserver = new StreamObserver<>() {
                @Override
                public void onNext(ImgID imgID) {
                    System.out.println("Image uploaded successfully. ID: " + imgID.getId());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Upload error: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("Upload finished!");
                }
            };

            StreamObserver<Image> requestObserver = asyncStub.imageProcessing(responseObserver);

            DataImage metadata = DataImage.newBuilder()
                    .setFilename(fileName)
                    .build();

            Image dataMsg = Image.newBuilder()
                    .setData(metadata)
                    .build();

            requestObserver.onNext(dataMsg);

            FileInputStream fis = new FileInputStream(filePath);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                Image chunk = Image.newBuilder()
                        .setBlock(com.google.protobuf.ByteString.copyFrom(buffer, 0, bytesRead))
                        .build();
                requestObserver.onNext(chunk);
            }

            fis.close();

            Image done = Image.newBuilder()
                    .setMessage("EOF")
                    .build();
            requestObserver.onNext(done);

            requestObserver.onCompleted();

            Thread.sleep(1000);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void downloadImage() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Image ID to download: ");
            String id = scanner.nextLine();

            ImgID request = ImgID.newBuilder().setId(id).build();
            var stream = blockingStub.downloadImage(request);

            // qualquer formato***
            String format = "png";
            if (id.toLowerCase().contains("jpg")) format = "jpg";
            FileOutputStream fos = new FileOutputStream("downloaded_" + id + ".png");
            System.out.println("Downloading image...");

            while (stream.hasNext()) {
                Image img = stream.next();
                if (img.hasBlock()) {
                    fos.write(img.getBlock().toByteArray());
                }
            }

            fos.close();
            System.out.println("Image saved as downloaded_" + id + ".png");

        } catch (Exception e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }

    public void showStatus() {
        try  {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Image ID to download: ");
            String id = scanner.nextLine();

            ImgID request = ImgID
                    .newBuilder()
                    .setId(id)
                    .build();
            StatusInfo response = blockingStub.consultStatus(request);

            System.out.println("\n Image Status is equal:");
            System.out.println("**********************************************");
            System.out.println("ID          : " + response.getId());
            System.out.println("Status      : " + response.getStatus());
            System.out.println("Filename    : " + response.getFilename());
            System.out.println("Input Path  : " + response.getInputPath());
            System.out.println("Output Path : " + response.getOutputPath());
            System.out.println("Host        : " + response.getHost());
            System.out.println("Port        : " + response.getPort());
            System.out.println("***********************************************");
        } catch (Exception e) {
            System.err.println("Error consulting: " + e.getMessage());
        }
    }

    public void close() {
        channel.shutdown();
    }

    public void uploadImageFromPath() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Path of the image to upload: ");
        String filePath = scanner.nextLine();

        filePath = filePath.replace("\"", ""); // remove aspas se o utilizador colar o caminho com ""
        filePath = filePath.replace("\\", "/"); // converte \ para / (compatível com todos OS)

        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);


        //String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        StreamObserver<ImgID> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ImgID imgID) {
                System.out.println("ID: " + imgID.getId());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();

            }

            @Override
            public void onCompleted() {
                System.out.println("Upload done.");

            }
        };

        StreamObserver<Image> requestObserver = asyncStub.imageProcessing(responseObserver);
        DataImage metadata = DataImage.newBuilder().setFilename(fileName).build();
        requestObserver.onNext(Image.newBuilder().setData(metadata).build());
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                requestObserver.onNext(Image.newBuilder()
                        .setBlock(ByteString.copyFrom(buffer,0,bytesRead)).build());
            }
        }
        requestObserver.onNext(Image.newBuilder().setMessage("EOF").build());
        requestObserver.onCompleted();
        Thread.sleep(500); // esperar resposta
    }

}