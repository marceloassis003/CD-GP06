package serverapp;

import io.grpc.stub.StreamObserver;
import primeToRingStubs.PrimeInfo;
import primeToRingStubs.PrimeRegistInfo;
import primeToRingStubs.PrimeToRingServiceGrpc;

import java.util.List;
import java.util.UUID;



public class PrimeServerHandler extends PrimeToRingServiceGrpc.PrimeToRingServiceImplBase {

    private List<PrimeContainer> primes;
    public PrimeServerHandler(List<PrimeContainer> servers) {
        primes = servers;
    }

    @Override
    public void registServer(PrimeRegistInfo request, StreamObserver<PrimeInfo> responseObserver) {
        System.out.println("Registering new Prime Server with ip: " + request.getIp() + "and port: " + request.getPort());
        UUID uuid = UUID.randomUUID();
        PrimeInfo newPrime = PrimeInfo.newBuilder()
                .setServerInfo(request)
                .setUid(uuid.toString())
                .build();
        //Last prime references this new Prime Server
        if (!primes.isEmpty()){
            System.out.println("Updating existing Prime Servers with this new");
            primes.get(primes.size() - 1).responseObserver.onNext(newPrime);
        }
        PrimeContainer primeContact = new PrimeContainer(newPrime,responseObserver);
        System.out.println("Add new Prime Server");
        primes.add(primeContact);
        //The next of the new Prime Server is the first prime server of the list
        System.out.println("Updating the next Prime Server of the newly added Prime Server");
        System.out.println("Current number of prime servers: " + primes.size());
        PrimeContainer nextPrimeData = primes.get(0);
        PrimeRegistInfo info = PrimeRegistInfo.newBuilder().setIp(nextPrimeData.ip).setPort(nextPrimeData.port).build();
        PrimeInfo nextPrimeInfo = PrimeInfo.newBuilder().setServerInfo(info).setUid(nextPrimeData.uId).build();
        responseObserver.onNext(nextPrimeInfo);
    }

}
