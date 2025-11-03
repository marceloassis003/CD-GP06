package app;

import clientToImageServiceStubs.ClientToImageserverGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import redis.clients.jedis.Jedis;



public class ImageServer {

    public static  void main(String[] args) {
        // arguments for execution
        if (args.length < 3) {
            System.out.println("Use: java -jar ImageServer.jar <serverPort> <redisHost> <redisPort>");
            return;
        }
        String host = "localhost";
        int port = 8080;

        int serverport = Integer.parseInt(args[0]);
        String redisHost = args[1];
        int redisPort = Integer.parseInt(args[2]);

        try {
            System.out.println("Start connection with Global Redis " + redisHost + ":" + redisPort);
            Jedis redis = new Jedis(redisHost, redisPort);

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

