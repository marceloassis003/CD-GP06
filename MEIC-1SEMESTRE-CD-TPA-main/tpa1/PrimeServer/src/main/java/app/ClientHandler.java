package app;


import clientToPrimeStubs.ClientToPrimeServiceGrpc;

import clientToPrimeStubs.Number;
import clientToPrimeStubs.Result;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import primeToPrimeStubs.PrimeToPrimeServiceGrpc;
import primeToPrimeStubs.RingMessage;

import redis.clients.jedis.Jedis;

import java.util.UUID;


public class ClientHandler extends ClientToPrimeServiceGrpc.ClientToPrimeServiceImplBase {


    private final PrimeManager nextPrime;
    private final RingMessageManager ringMessageManager;
    private final Jedis jedis;


    public ClientHandler(PrimeManager nextPrimeInfo, RingMessageManager ringMessageManager, Jedis jedis) {
        this.nextPrime = nextPrimeInfo;
        this.ringMessageManager = ringMessageManager;
        this.jedis = jedis;
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
    public StreamObserver<Number> isPrime(StreamObserver<Result> responseObserver) {
        System.out.println("Returning computable logic");
        return new StreamObserver<>() {
            @Override
            public void onNext(Number number) {
                long num = number.getValue();

                System.out.println("Verifying if prime number: " + num + "is saved");
                // access the redis to verify if result is saved
                String isPrime = jedis.get(String.valueOf(num));
                if (isPrime != null){
                    System.out.println("Prime number is saved, answering request");
                    Result result = Result.newBuilder().setIsPrime(Boolean.parseBoolean(isPrime)).setNumber(num).build();
                    responseObserver.onNext(result);
                }else {
                    //Ask the ring
                    System.out.println("Prime number is not saved, asking the ring and saving client request");
                    PrimeToPrimeServiceGrpc.PrimeToPrimeServiceStub primeStub = nextPrime.getStub();
                    StreamObserver<RingMessage> forwardMessage = primeStub.nextServer(emptyStreamObserver);
                    UUID requestId = UUID.randomUUID();
                    RingMessage message =
                            RingMessage.newBuilder()
                                    .setRequestId(requestId.toString())
                                    .setIsPrime(false)
                                    .setIsDone(false)
                                    .setPrimeNumber(num)
                                    .build();
                    ringMessageManager.saveRequest(message,responseObserver);
                    forwardMessage.onNext(message);
                }


                //if the number is not in the redis call nextPrimeServer

                //if the uid of the message is the same run the container to verify the number.
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {

            }
        };
    }

}
