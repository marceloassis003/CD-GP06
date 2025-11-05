package serverapp;

import ImageToManagerStubs.ImageRegistInfo;
import ImageToManagerStubs.ImageToManagerServiceGrpc;
import ImageToManagerStubs.ImageInfo;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;


public class ImageServerHandler extends ImageToManagerServiceGrpc.ImageToManagerServiceImplBase {

    private final Jedis redis;
    private final List<ImageInfo> registeredServers;
    private final int redisPort;

    public ImageServerHandler(Jedis redis, List<ImageInfo> registeredServers, int redisPort) {
        this.redis = redis;
        this.registeredServers = registeredServers;
        this.redisPort = redisPort;
    }

    @Override
    public void registServer(ImageRegistInfo request, StreamObserver<ImageInfo> responseObserver) {
        System.out.println("[ManagerServer] Novo pedido de registo de ImgServer:");
        System.out.println("   -> IP: " + request.getIp());
        System.out.println("   -> Porto: " + request.getPort());

        if (request.getPort() == ManagerServer.getManagerPort()) {
            System.err.println("Manager Error not possible register with same manager port !");
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Image server cannot use the same port")
                    .asRuntimeException());
            return;
        }

        String uuid = UUID.randomUUID().toString(); // gera sempre 36 caracteres


        // Cria objeto de resposta
        ImageInfo serverInfo = ImageInfo.newBuilder()
                .setUid(uuid)
                .setRedisPort(redisPort)
                .build();

        registeredServers.add(serverInfo);

        // set redis global
        String Key = "SERVER:" + uuid;
        redis.hset(Key, "ip", request.getIp());
        redis.hset(Key, "port", String.valueOf(request.getPort()));
        redis.hset(Key, "status", "AVAILABLE");

        responseObserver.onNext(serverInfo);
        responseObserver.onCompleted();

        System.out.println("[Manager Server] ImageServer regist with UID: " + uuid);
    }
}


