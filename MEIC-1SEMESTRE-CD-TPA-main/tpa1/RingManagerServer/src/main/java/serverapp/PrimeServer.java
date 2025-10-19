package serverapp;

import io.grpc.stub.StreamObserver;
import primeToRingStubs.PrimeInfo;

class PrimeContainer{
    public String ip;
    public int port;
    public String uId;
    public int clients = 0;

    public StreamObserver<PrimeInfo> responseObserver;

    public PrimeContainer(PrimeInfo primeInfo, StreamObserver<PrimeInfo> responseObserver) {
        this.ip = primeInfo.getServerInfo().getIp();
        this.port = primeInfo.getServerInfo().getPort();
        this.uId = primeInfo.getUid();
        this.responseObserver = responseObserver;
    }
}