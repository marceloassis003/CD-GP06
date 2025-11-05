package app;

import clientToImageServiceStubs.ClientToImageserverGrpc;
import clientToImageServiceStubs.Image;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ImageToManagerStubs.ImageRegistInfo;
import ImageToManagerStubs.ImageInfo;
import ImageToManagerStubs.ImageToManagerServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.internal.JsonUtil;
import io.grpc.stub.StreamObserver;
import redis.clients.jedis.Jedis;



public class ImageServer {

    private static  final String managerHost = "localhost";
    private static final int managerPort = 8001; //50051;


    public static void RegistServer (String managerHost, int managerPort, String IpLocal, int portLocal ){

        System.out.println("Image Server request Register with Manager: " + managerHost + ":" + managerPort);
        try {
            // create chanel
            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(managerHost, managerPort)
                    .usePlaintext()
                    .build();

            // stub bloqueante (sincrono)
            ImageToManagerServiceGrpc.ImageToManagerServiceBlockingStub stub =
                    ImageToManagerServiceGrpc.newBlockingStub(channel);

            // message register
            ImageRegistInfo regis = ImageRegistInfo.newBuilder()
                    .setIp(IpLocal)
                    .setPort(portLocal)
                    .build();
            // send request
            ImageInfo resp = stub.registServer(regis);

            // show result
            System.out.println("Image Server reistered !!!");
            System.out.println("UID: " + resp.getUid());
            System.out.println("Please connect in my Redis in port -> " + resp.getRedisPort());

            channel.shutdown();

        }catch (Exception e) {
            System.err.println("Failed to register " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static  void main(String[] args) {
        // arguments for execution
        if (args.length < 3) {
            System.out.println("Use: java -jar ImageServer.jar <serverPort> <redisHost> <redisPort>");
            return;
        }
        String host = "localhost";
        //int port = 8080;

        int serverport = Integer.parseInt(args[0]);
        String redisHost = args[1];
        int redisPort = Integer.parseInt(args[2]);

        try {
            System.out.println("Start connection with Global Redis " + redisHost + ":" + redisPort);
            Jedis redis = new Jedis(redisHost, redisPort);

            RegistServer(managerHost, managerPort, host, serverport);

            System.out.println("Start Imageserver in port " + serverport + ".....");
            Server server = ServerBuilder
                    .forPort(serverport)
                    .addService(new ImageHandler(redis, redisHost, redisPort, serverport))
                    .build()
                    .start();

            System.out.println("ImageServer start succefully !!");
            System.out.println("GRPC port " + serverport);
            System.out.println("Redis connected: " + redisHost + ":" + redisPort);

            server.awaitTermination();


        } catch (Exception e) {
            System.err.println("Error start ImageServer: " + e.getMessage());
            e.printStackTrace();
        }

    }
}

