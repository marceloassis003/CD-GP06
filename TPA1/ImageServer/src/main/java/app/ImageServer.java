package app;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import primeToRingStubs.PrimeInfo;
import primeToRingStubs.PrimeRegistInfo;
import primeToRingStubs.PrimeToRingServiceGrpc;
import redis.clients.jedis.Jedis;


public class ImageServer {

    private static ManagedChannel channel;
    private static PrimeToRingServiceGrpc.PrimeToRingServiceStub ringStub;
    private static int ringServicePort = 8500;


    private static void registerToManager(String ringServiceIP,String primeIp,int port) {
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
                nextImageManager.setPrimeServer(nextPrime);
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
        String host = "localhost";
        int port = 8080;
        int redisPort = port + 10;

        try {
            if (args.length > 0) {
                host = args[0];
                port = Integer.parseInt(args[1]);
                redisPort = Integer.parseInt(args[2]);
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
                .addService(new ImageHandler(nextImageManager,manager,jedis))
                    .addService(new ImageToManagerServer(nextImageManager,manager,jedis,container))
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

