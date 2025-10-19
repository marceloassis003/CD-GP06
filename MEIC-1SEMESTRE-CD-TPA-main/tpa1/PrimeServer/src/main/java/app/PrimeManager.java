package app;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import primeToPrimeStubs.PrimeToPrimeServiceGrpc;
import primeToRingStubs.PrimeRegistInfo;

public class PrimeManager{

    private PrimeRegistInfo primeRegistInfo;

    public PrimeToPrimeServiceGrpc.PrimeToPrimeServiceStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(primeRegistInfo.getIp(), primeRegistInfo.getPort())
                // Channels are secure by default (via SSL/TLS). Here we disable
                // TLS to avoid needing certificates.
                .usePlaintext()
                .build();
        return PrimeToPrimeServiceGrpc.newStub(channel);
    }

    public void setPrimeServer(PrimeRegistInfo nextPrime) {
        primeRegistInfo = nextPrime;
    }
}
