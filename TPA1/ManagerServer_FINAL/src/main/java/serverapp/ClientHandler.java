package serverapp;


import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ClientToManagerStubs.ImageInfo;
import ClientToManagerStubs.ClientToManagerServiceGrpc;
import ClientToManagerStubs.ImgRegist;
import redis.clients.jedis.Jedis;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ClientHandler extends ClientToManagerServiceGrpc.ClientToManagerServiceImplBase {
    //private ImgSVManager manager;

    private final Jedis redis;

    public ClientHandler(Jedis redis) {
        this.redis = redis;
    }

    @Override
    public void getServer(Empty request, StreamObserver<ImageInfo> responseObserver) {
        try {
            Set<String> Keys = redis.hkeys("Server:");
            if (Keys.isEmpty()) {
                System.out.println("Manager not have image server avaliable yet.....");
                responseObserver.onError(
                        Status.UNAVAILABLE
                                .withDescription("not have image server avaliable.")
                                .asRuntimeException());
                return;
            }
            List<Map<String, String>> servers = Keys.stream()
                    .map(redis::hgetAll)
                    .filter(map -> map != null && map.containsKey("ip") && map.containsKey("port"))
                    .collect(Collectors.toList());

            servers.sort(Comparator.comparingInt(s -> Integer.parseInt(s.getOrDefault("clients", "0"))));

            Map<String, String> selected = servers.get(0);
            String ip = selected.get("ip");
            int port = Integer.parseInt(selected.get("port"));
            String uid = selected.getOrDefault("uid", "unknown");

            System.out.println("Manager add new image service on new client " + ip +":"+ port);

            int clients = Integer.parseInt(selected.getOrDefault("clients", "0")) +1;
            redis.hset("SERVER:" + uid, "clients", String.valueOf(clients));

            ImgRegist serverInfo = ImgRegist.newBuilder()
                    .setIp(ip)
                    .setPort(port)
                    .build();

            ImageInfo info = ImageInfo.newBuilder()
                    .setUid(uid)
                    .setServerInfo(serverInfo)
                    .build();

            responseObserver.onNext(info);
            responseObserver.onCompleted();

        } catch (Exception e) {
            System.err.println("Manager erro selected Image Server: " + e.getMessage() );
            e.printStackTrace();
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Error obtain image server.")
                            .withCause(e)
                            .asRuntimeException()
            );
        }

    }
}

