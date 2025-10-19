package clientApp;

import clientToPrimeStubs.ClientToPrimeServiceGrpc;
import clientToPrimeStubs.Number;
import clientToPrimeStubs.Result;
import clientToRingStubs.*;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;


import java.util.Scanner;


public class Client {

    private static String svcIP = "localhost";//RingManager
    //private static String svcIP = "35.246.73.129";
    private static int svcPort = 8500;
    private static ManagedChannel ringManagerChannel;
    private static ManagedChannel primeServerChannel;
    private static ClientToRingServiceGrpc.ClientToRingServiceBlockingStub ringManagerBlockingStub;



    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                svcIP = args[0];
                svcPort = Integer.parseInt(args[1]);
            }
            System.out.println("connect to RingManager"+svcIP+":"+svcPort);
            ringManagerChannel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
            ringManagerBlockingStub = ClientToRingServiceGrpc.newBlockingStub(ringManagerChannel);
            //noBlockStub = ClientToRingServiceGrpc.newStub(ringManagerChannel);


            PrimeInfo serverInfo = ringManagerBlockingStub.getPrimeServer(Empty.newBuilder().build());
            System.out.println("Connected to Prime server with id: "+ serverInfo.getUid() + "and address "+
                    serverInfo.getServerInfo().getIp() + ":" + serverInfo.getServerInfo().getPort()
            );
            primeServerChannel = ManagedChannelBuilder.forAddress(
                    serverInfo.getServerInfo().getIp(),
                    serverInfo.getServerInfo().getPort()
            ).usePlaintext().build();
            ClientToPrimeServiceGrpc.ClientToPrimeServiceStub noBlockingPrimeStub = ClientToPrimeServiceGrpc.newStub(primeServerChannel);

            StreamObserver<Result> res = new StreamObserver<Result>() {

                @Override
                public void onNext(Result result) {
                    System.out.println("The number "+ result.getNumber());
                    if (result.getIsPrime()){
                        System.out.println("Is a prime number");
                    }else {
                        System.out.println("Is not a prime number");
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("Error: " + throwable.getMessage());
                    throwable.printStackTrace();
                }

                @Override
                public void onCompleted() {
                    System.out.println("Done");
                }
            };
            StreamObserver<Number> numbers = noBlockingPrimeStub.isPrime(res);
            while (true) {
                switch (Menu()) {
                    case 1:  // Is prime?
                        System.out.println("Please insert the number:");
                        Scanner number = new Scanner(System.in);
                        long value = number.nextLong();

                        System.out.println("Building request of number: ");
                        Number sendNumber = Number.newBuilder().setValue(value).build();
                        System.out.println("Sending to Server");
                        numbers.onNext(sendNumber);
                        break;
                    case 99:
                        System.exit(0);
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Case1 - Check Prime server");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 4) || op == 99));
        return op;
    }


}