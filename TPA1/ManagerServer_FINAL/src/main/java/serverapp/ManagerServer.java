package serverapp;

import ImageToManagerStubs.ImageInfo;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import redis.clients.jedis.Jedis;

import java.lang.ref.Cleaner;
import java.util.ArrayList;
import java.util.List;



public class ManagerServer
{
    private static int svcPort = 8001;
    private static int redisPort = 6379;

    public static int getManagerPort(){
        return svcPort;
    }

    public static void main(String[] args)
    {
        List<ImageInfo> registeredServers = new ArrayList<>();

        try
        {
            if (args.length > 0) svcPort = Integer.parseInt(args[0]);
            if (args.length > 1) svcPort = Integer.parseInt(args[1]);

            // connect Redis Global (in host)
            String redisHost = "localhost";
            Jedis redis = new Jedis(redisHost, redisPort);

            // instance handler
            ImageServerHandler imgServHandler = new ImageServerHandler(redis, registeredServers, redisPort);
            ClientHandler clientHandler = new ClientHandler(redis);

            Server svc = ServerBuilder
                    .forPort(svcPort)
                    .addService(imgServHandler)
                    .build()
                    .start();

            System.out.println("Server started listening on port: " + svcPort);
            System.out.println("Start connection with redis:" + redisHost + ":" + redisPort);


            svc.awaitTermination();

        } catch (Exception e)
        {
            System.err.println("Manager error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

