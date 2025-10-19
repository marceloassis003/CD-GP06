package clientapp;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import regStubs.RegisterServiceGrpc;
import regStubs.ServerInfo;
import svcstubs.ImageBlock;
import svcstubs.ImageData;
import svcstubs.ImageId;
import svcstubs.SvcServiceGrpc;

import java.io.*;
import java.util.*;


public class Client {

    private static String registerIp = "34.90.130.219";
    //private static String svcIP = "35.246.73.129";
    private static int registerPort = 8500;
    private static ManagedChannel registerChannel;
    private static ManagedChannel scvChannel;
    private static RegisterServiceGrpc.RegisterServiceStub noBlockRegisterStub;
    private static SvcServiceGrpc.SvcServiceStub noBlockSvcStub;
    private static String downloadDir = System.getProperty("user.dir") + "/tpa2/testDir/download";
    private static ServerInfo serverInfo;

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                registerIp = args[0];
                registerPort = Integer.parseInt(args[1]);
                downloadDir = args[2];
            }else {
                System.out.println("Images will be download to: "+ new File(downloadDir).getAbsolutePath());
            }
            System.out.println("connect to "+ registerIp +":"+ registerPort);
            registerChannel = ManagedChannelBuilder.forAddress(registerIp, 8500)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();

            noBlockRegisterStub = RegisterServiceGrpc.newStub(registerChannel);
//            //TODO IMPORTANT TO DELETE TEST ONLY
//            scvChannel = ManagedChannelBuilder.forAddress("localhost", 8500)
//                    .usePlaintext()
//                    .build();
//            noBlockSvcStub = SvcServiceGrpc.newStub(scvChannel);


            while (true) {
                switch (Menu()) {
                    case 1:  // Pedir IP e PORT do serviço através do servidor de registo
                        getService();
                        break;
                    case 2:
                        uploadImage();
                        break;
                    case 3:
                        downloadImage();
                        break;
                    case 99:
                        System.exit(0);
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static int Menu() {
        int op = 0;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Case1 - Get server IP and PORT");
            System.out.println(" 2 - Case2 - Upload Image");
            System.out.println(" 3 - Case3 - Download Image");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            try {
                op = scan.nextInt();
            }catch (InputMismatchException e) {
                System.out.println("Invalid Option");
                op = 0;
            }
        } while (!((op >= 0 && op <= 4) || op == 99));
        return op;
    }

    private static void getService() {
        System.out.println("Getting Service IP and PORT");
        ServerInfo.Builder myService = ServerInfo.newBuilder();
        StreamObserver<ServerInfo> res = new StreamObserver<ServerInfo>() {
            @Override
            public void onNext(ServerInfo serverInfo) {
                if (serverInfo.getIp().isEmpty() || serverInfo.getPort() == 0) {
                    System.out.println("No services available");
                    return;
                }
                myService.setIp(serverInfo.getIp()).setPort(serverInfo.getPort());
                System.out.println("My service IP " + myService.getIp() + " and PORT " + myService.getPort());
                serverInfo = myService.build();

                scvChannel = ManagedChannelBuilder.forAddress(serverInfo.getIp(), serverInfo.getPort())
                        .usePlaintext()
                        .build();
                noBlockSvcStub = SvcServiceGrpc.newStub(scvChannel);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error" + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }
        };
        noBlockRegisterStub.getService(Empty.newBuilder().build(),res);
    }
    private static void uploadImage(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert file path");
        String pathToImage = scanner.nextLine();

        //String pathToImage = "C:/Isel/meic/1semestre/CD/tp/tpa/tpa2/testDir/upload/image.jpg";
        File file = new File(pathToImage);
        if (!file.exists()) {
            System.out.println("File does not exist");
            return;
        }

        //Validar o tipo da imagem
        String imageFullName;
        try {
            imageFullName = isValidImage(pathToImage);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Insert tag");

        int limit = 5;
        List<String> tags = new ArrayList<>();
        while (true) {
            tags.add(scanner.nextLine());
            if (tags.size() == limit) {
                break;
            }
            System.out.println("Insert more tags? Y or N");
            if (scanner.nextLine().toLowerCase().equals("n")) {
                break;
            }

        }
        //Preparar stream para retorno
        StreamObserver<ImageId> res = new StreamObserver<ImageId>() {

            ImageId image;
            @Override
            public void onNext(ImageId imageId) {
                image = imageId;
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error" + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Image with id: " + image.getId() + " uploaded");
            }
        };

        StreamObserver<ImageBlock> imageStream = noBlockSvcStub.uploadImage(res);
        //Recolher do utilizador
        ImageData data = ImageData.newBuilder()
                .setFilename(imageFullName)
                .addAllTags(tags)
                .build();
        ImageBlock metaData = ImageBlock.newBuilder().setData(data).build();
        imageStream.onNext(metaData);

        byte[] buffer = new byte[1024];
        try (InputStream input = new FileInputStream(file)) {
            while (input.read(buffer) >= 0) {
                try {
                    ByteString bytes = ByteString.copyFrom(buffer);
                    imageStream.onNext(ImageBlock.newBuilder().setBlock(bytes).build());
                } catch (Exception ex) {
                    imageStream.onError(ex);
                }
            }
            imageStream.onCompleted();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Image sent");
    }
    private static String isValidImage(String pathToImage) throws RuntimeException {
        String parts = pathToImage.replace("\\", "/");
        String[] pathChuncks = parts.split("/");//Separar diretorias do nome do ficheiro
        String imageFullName = pathChuncks[pathChuncks.length - 1];//Selecionar o nome do ficheiro
        String[] imageSplit = imageFullName.split("\\.");
        if (imageSplit.length < 2) {
            throw new RuntimeException("Invalid image format");
        }
        String imageType = imageFullName.split("\\.")[1];//Extrair extensão do ficheiro
        //Validar extensão
        if(!imageType.equals("png") && !imageType.equals("jpeg") && !imageType.equals("jpg")) {
            throw new RuntimeException("Image type must be png, jpeg or jpg");
        }
        return imageFullName;
    }
    private static void downloadImage() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert image ID");
        String imageId = scanner.nextLine();

        try {
            imageId = isValidImage(imageId);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Downloading image with id: " + imageId);
        ImageId image = ImageId.newBuilder().setId(imageId).build();
        //Stream observer que baixa os bytes da imagem solicitada
        StreamObserver <ImageBlock> imageStream = new StreamObserver<ImageBlock>() {
           final File imageFile =new File(downloadDir, image.getId());
           final FileOutputStream writer = new FileOutputStream(imageFile);

            @Override
            public void onNext(ImageBlock imageBlock) {
                if (imageBlock.getOptionsCase() != ImageBlock.OptionsCase.BLOCK){
                    System.out.println("Invalid image Block");
                    throw new RuntimeException("Invalid image block");
                }else {
                    try {
                        writer.write(imageBlock.getBlock().toByteArray());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error" );
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (imageFile.exists()) {
                    System.out.println("Image exists, deleting:" + imageFile.getAbsolutePath());
                    if (imageFile.delete()){
                        System.out.println("Image deleted");
                    }
                    else {
                        System.out.println("Failed to delete image");
                    }
                }
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Terminating writer");
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Failed with: "+ e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        };
        noBlockSvcStub.downloadImage(image,imageStream);
    }


}
