package serverapp;



import io.grpc.ServerBuilder;
import primeToRingStubs.PrimeRegistInfo;

import java.util.ArrayList;
import java.util.List;


public class RingServer {

    private static int svcPort = 8500;

    public static void main(String[] args) {
        List<PrimeContainer> primeServers = new ArrayList<>();
        try {
            if (args.length > 0) svcPort = Integer.parseInt(args[0]);
            PrimeServerHandler primeServerHandler = new PrimeServerHandler(primeServers);
            io.grpc.Server svc = ServerBuilder
                .forPort(svcPort)
                .addService(primeServerHandler).addService(new ClientHandler(primeServers))
                .build();
            svc.start();
            System.out.println("Server started, listening on " + svcPort);


            svc.awaitTermination();
            svc.shutdown();
            ;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

