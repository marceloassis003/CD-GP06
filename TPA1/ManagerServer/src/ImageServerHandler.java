// import imageToManagerStubs.ImageToManagerServiceGrpc;

import java.util.List;
import java.util.UUID;

public class ImageServerHandler extends ImageToManagerServiceGrpc.ImageToServiceImplBase
{
    private List<ImageContainer> images;

    public ImageServerHandler(List<ImageContainer> servers)
    {
        images = servers;
    }

    @Override
    public void imageServer(ImageRegistInfo request, StreamObserver<ImageInfo> responseObserver) {
        System.out.println("Registering new Image Server with ip: " + request.getIp() + "and port: " + request.getPort());

        UUID uuid = UUID.randomUUID();

        ImageInfo newImageSV = ImageInfo.newBuilder()
                .setServerInfo(request)
                .setUid(uuid.toString())
                .build();
        
        //Last prime references this new Prime Server
        if (!images.isEmpty()){
            System.out.println("Updating existing Image Servers with this new");
            images.get(images.size() - 1).responseObserver.onNext(newPrime);
        }
}
