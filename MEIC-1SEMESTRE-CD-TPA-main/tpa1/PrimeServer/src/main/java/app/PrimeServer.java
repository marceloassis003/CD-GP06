package app;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import primeToRingStubs.PrimeInfo;
import primeToRingStubs.PrimeRegistInfo;
import primeToRingStubs.PrimeToRingServiceGrpc;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrimeServer {

    private static ManagedChannel channel;
    private static PrimeToRingServiceGrpc.PrimeToRingServiceStub ringStub;
    private static PrimeManager nextPrimeManager = new PrimeManager();
    private static int ringServicePort = 8500;


    private static void registerToRingManager(String ringServiceIP,String primeIp,int port) {
        // PrimeServer as a client of RingManager

        // Channels are secure by default (via SSL/TLS). Here we disable
        // TLS to avoid needing certificates.
        channel = ManagedChannelBuilder.forAddress(ringServiceIP, ringServicePort)
                // Channels are secure by default (via SSL/TLS). Here we disable
                // TLS to avoid needing certificates.
                .usePlaintext()
                .build();
        ringStub = PrimeToRingServiceGrpc.newStub(channel);

        PrimeRegistInfo request = PrimeRegistInfo.newBuilder()
                .setIp(primeIp)//To check when using on GPC
                .setPort(port)// To assign when switching to cmd arguments
                .build();

        ringStub.registServer(request, new StreamObserver<PrimeInfo>() {
            @Override
            public void onNext(PrimeInfo value) {
                PrimeRegistInfo nextPrime =
                        PrimeRegistInfo.newBuilder()
                                .setIp(value.getServerInfo().getIp())
                                .setPort(value.getServerInfo().getPort()).build();
                System.out.println("Next prime server updated to ip: " + value.getServerInfo().getIp() + "and port: " + value.getServerInfo().getPort());
                nextPrimeManager.setPrimeServer(nextPrime);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {

            }
        }
        );
    }


        public static void main(String[] args) {
        String ringServiceIP = "localhost";
        String primeIp = "localhost";
        int port = 8080;
        try {
            if (args.length > 0) {
                ringServiceIP = args[0];
                primeIp = args[1];
                port = Integer.parseInt(args[2]);
            }
            String redisAddress = primeIp;
            int redisPort = port + 10;

            RedisManager container = new RedisManager(redisAddress,redisPort);
            container.startRedis();
            Jedis jedis = new Jedis(redisAddress, redisPort);
            registerToRingManager(ringServiceIP,primeIp,port);
            RingMessageManager manager = new RingMessageManager();
            io.grpc.Server svc = ServerBuilder
                .forPort(port)
                .addService(new ClientHandler(nextPrimeManager,manager,jedis))
                    .addService(new PrimeToPrimeHandler(nextPrimeManager,manager,jedis,container))
                .build();
            svc.start();
            System.out.println("Server started, listening on " + port);


            svc.awaitTermination();
            svc.shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

