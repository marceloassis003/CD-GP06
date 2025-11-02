// Falta imports

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientHandler extends ClientToManagerSVGrpc.ClientToManagerSVServiceImplBase {
    //private ImgSVManager manager;
    private List<ImgSVContainer> imgServers = new ArrayList<>();
    public ClientHandler(List<ImgSVContainer> servers) {
        imgServers = servers;
    }


    // a) getServer [ManagerSV -> newClient]
    @Override
    public void getImageServer(Empty request, StreamObserver<ImageSVInfo> responseObserver) {

        while (imgServers.isEmpty()){
            Thread.yield();
            //wait for servers to be available
        }
        imgServers.sort(new Comparator<ImgSVContainer>() {
            @Override
            public int compare(ImgSVContainer o1, ImgSVContainer o2) {
                return Integer.compare(o1.clients, o2.clients);
            }
        });

        System.out.println("Returning image server with the least clients");
        ImgSVContainer imageServer = imgServers.get(0);

        System.out.println("Returning image with ip: " + imageServer.ip + "and port: " + imageServer.port + "and with: " + imageServer.clients + "clients");
        imageServer.clients = imageServer.clients + 1;

        ImageSVRegistInfo data = ImageSVRegistInfo.newBuilder().setIp(imageServer.ip).setPort(imageServer.port).build();
        ImageSVInfo imgSv = ImageSVInfo.newBuilder().setServerInfo(data).setUid(imageServer.uId).build();
        responseObserver.onNext(imgSv);

        responseObserver.onCompleted();
    }
}
