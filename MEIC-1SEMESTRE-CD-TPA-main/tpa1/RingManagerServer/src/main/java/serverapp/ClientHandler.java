package serverapp;

import clientToRingStubs.ClientToRingServiceGrpc;
import clientToRingStubs.PrimeInfo;
import clientToRingStubs.PrimeRegistInfo;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientHandler extends ClientToRingServiceGrpc.ClientToRingServiceImplBase {
    //private PrimeManager manager;
    private List<PrimeContainer> primes = new ArrayList<>();
    public ClientHandler(List<PrimeContainer> servers) {
        primes = servers;
    }

    @Override
    public void getPrimeServer(Empty request, StreamObserver<PrimeInfo> responseObserver) {

        while (primes.isEmpty()){
            Thread.yield();
            //wait for servers to be available
        }
        primes.sort(new Comparator<PrimeContainer>() {
            @Override
            public int compare(PrimeContainer o1, PrimeContainer o2) {
                return Integer.compare(o1.clients, o2.clients);
            }
        });
        System.out.println("Returning prime server with the least clients");
        PrimeContainer primeServer = primes.get(0);
        System.out.println("Returning prime with ip: " + primeServer.ip + "and port: " + primeServer.port + "and with: " + primeServer.clients + "clients");
        primeServer.clients = primeServer.clients + 1;
        PrimeRegistInfo data = PrimeRegistInfo.newBuilder().setIp(primeServer.ip).setPort(primeServer.port).build();
        PrimeInfo prime = PrimeInfo.newBuilder().setServerInfo(data).setUid(primeServer.uId).build();
        responseObserver.onNext(prime);
        responseObserver.onCompleted();
    }
}
