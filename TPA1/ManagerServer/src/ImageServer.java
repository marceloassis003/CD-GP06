package managersvapp;
import io.grpc.stub.StreamObserver;

// import primeToRingStubs.ImageInfo;
class ImgContainer
{
    public String ip;
    public int port;
    public String uId;
    public int clients = 0;
    public StreamObserver<ImageSVInfo> responseObserver;

    public PrimeContainer(ImageInfo imgInfo, StreamObserver<ImageInfo> responseObserver) {
        this.ip = imgInfo.getServerInfo().getIp();
        this.port = imgInfo.getServerInfo().getPort();
        this.uId = imgInfo.getUid();
        this.responseObserver = responseObserver;
    }
}
