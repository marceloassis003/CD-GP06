package app;

import clientToPrimeStubs.Result;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import primeToPrimeStubs.PrimeToPrimeServiceGrpc;
import primeToPrimeStubs.RingMessage;
import redis.clients.jedis.Jedis;


public class PrimeToPrimeHandler extends PrimeToPrimeServiceGrpc.PrimeToPrimeServiceImplBase {

    private int test1 = 4;
    private int test2 = 7;

    private final PrimeManager nextPrimeManager;
    private final RingMessageManager manager;
    private final Jedis jedis;
    private final RedisManager container;

    public PrimeToPrimeHandler(PrimeManager nextPrimeManager, RingMessageManager manager, Jedis jedis, RedisManager container) {
        this.nextPrimeManager = nextPrimeManager;
        this.manager = manager;
        this.jedis = jedis;
        this.container = container;
    }
    StreamObserver<Empty> emptyStreamObserver = new StreamObserver<Empty>() {
        @Override
        public void onNext(Empty empty) {
            //Do nothing
        }

        @Override
        public void onError(Throwable throwable) {
            //Do nothing
        }

        @Override
        public void onCompleted() {
            //Do nothing
        }
    };

    @Override
    public StreamObserver<RingMessage> nextServer(StreamObserver<Empty> responseObserver) {


        //check if it is prime
        return new StreamObserver<RingMessage>() {
            @Override
            public void onNext(RingMessage ringMessage) {
                System.out.println("---------------------------");
                System.out.println("Received neighbor Prime Server request");
                System.out.println("Checking if the client is mine");
                if (manager.isMyRequest(ringMessage.getRequestId())){
                    System.out.println("The client is mine, proceeding with final request validations...");
                    StreamObserver<Result> clientResponse = manager.getResponse(ringMessage.getRequestId());
                    Result.Builder res = Result.newBuilder();
                    if (ringMessage.getIsDone()){
                        System.out.println("The request has been completed by another Prime Server");
                       res.setIsPrime(ringMessage.getIsPrime()).setNumber(ringMessage.getPrimeNumber()).build();
                    }else {
                        System.out.println("The request was not completed, initiating calculations...");
                        try {
                            container.calculatePrime(ringMessage.getPrimeNumber());
                        } catch (InterruptedException e) {
                            System.out.println("Failed trying to calculate prime number");
                            throw new RuntimeException(e);
                        }
                        String isPrime = jedis.get(String.valueOf(ringMessage.getPrimeNumber()));
                        res.setIsPrime(Boolean.parseBoolean(isPrime)).setNumber(ringMessage.getPrimeNumber()).build();
                    }
                    clientResponse.onNext(res.build());
                }else {
                    System.out.println("The client is not Mine");
                    String isPrime = jedis.get(String.valueOf(ringMessage.getPrimeNumber()));
                    if (ringMessage.getIsDone()){
                        System.out.println("The request has been completed by another Prime Server");
                        if (isPrime == null){
                            System.out.println("Result yet not calculated, saving new value");
                            //Save in Redis
                            jedis.set(ringMessage.getPrimeNumber()+"",String.valueOf(ringMessage.getIsPrime()));
                        }
                    }else {
                        System.out.println("Request has yet been completed");
                        if (isPrime != null){
                            System.out.println("Completing request");
                            ringMessage = RingMessage.newBuilder()
                                            .setRequestId(ringMessage.getRequestId())
                                            .setIsPrime(Boolean.parseBoolean(isPrime))
                                            .setPrimeNumber(ringMessage.getPrimeNumber())
                                            .setIsDone(true)
                                            .build();
                        }
                    }
                    System.out.println("Sending to next server with request status at: " + ringMessage.getIsDone());
                    PrimeToPrimeServiceGrpc.PrimeToPrimeServiceStub primeStub = nextPrimeManager.getStub();
                    StreamObserver<RingMessage> passMessage = primeStub.nextServer(emptyStreamObserver);
                    passMessage.onNext(ringMessage);
                    System.out.println("---------------------------");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("error " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Done");
            }
        };
    }

}
