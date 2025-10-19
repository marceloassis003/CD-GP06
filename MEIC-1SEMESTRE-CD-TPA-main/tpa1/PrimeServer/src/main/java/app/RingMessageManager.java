package app;

import clientToPrimeStubs.Result;
import io.grpc.stub.StreamObserver;
import primeToPrimeStubs.RingMessage;

import java.util.Hashtable;

public class RingMessageManager{
    
    Hashtable<String, StreamObserver<Result>> list = new Hashtable<String,StreamObserver<Result>>();

    public void saveRequest(RingMessage message, StreamObserver<Result> responseObserver) {
        list.put(message.getRequestId(), responseObserver);
    }

    public boolean isMyRequest(String requestId) {
        return list.containsKey(requestId);
    }

    public StreamObserver<Result> getResponse(String requestId) {
        return list.remove(requestId);
    }
}
