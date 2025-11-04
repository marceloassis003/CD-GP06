// import imageToManagerStubs.ImageToManagerServiceGrpc;

import java.util.List;
import java.util.UUID;

public class ImageServerHandler extends ImageToManagerServiceGrpc.ImageToManagerServiceImplBase {

    private final List<ImageContainer> images;
    private final int redisPort;

    public ImageServerHandler(List<ImageContainer> images, int redisPort) {
        this.images = images;
        this.redisPort = redisPort;
    }

    @Override
    public void imageServer(ImageRegistInfo request, StreamObserver<ImageInfo> responseObserver) {
        System.out.println("[ManagerServer] New request of registration in a ImgServer:");
        System.out.println("   -> IP: " + request.getIp());
        System.out.println("   -> Port: " + request.getPort());

        UUID uuid = UUID.randomUUID();

        // Creates a new object for response
        ImageInfo newImageSV = ImageInfo.newBuilder()
                .setServerInfo(request)
                .setUid(uuid.toString())
                .setRedisPort(redisPort)
                .build();

        //Last image references this new ImageServer
        if (!images.isEmpty()){
            System.out.println("Updating existing Image Servers with this new");
            images.get(images.size() - 1).responseObserver.onNext(newImageSV);
        }

        responseObserver.onNext(newImageSV);
        responseObserver.onCompleted();

        System.out.println("[ManagerServer] ImgServer registred with UID=" + uuid.toString() + "-" + redisPort);
}
