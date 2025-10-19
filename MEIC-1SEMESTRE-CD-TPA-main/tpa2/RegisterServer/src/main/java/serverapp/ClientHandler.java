//package serverapp;
//
//import io.grpc.stub.StreamObserver;
//import regStubs.RegisterServiceGrpc;
//import regStubs.ServerInfo;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class ClientHandler   extends RegisterServiceGrpc.RegisterServiceImplBase{
//    static List<ServiceInfo> services = new ArrayList<>();
//
//    @Override
//    public void getService (com.google.protobuf.Empty request, StreamObserver<ServerInfo> responseObserver){
//        System.out.println("Returning service info");
//        if (services.isEmpty()){
//            ServerInfo service = ServerInfo.newBuilder().setIp("0").setPort(0).build();
//            System.out.println("No services available");
//            responseObserver.onNext(null);
//        } else {
//            Collections.sort(services);
//            ServiceInfo serviceInfo = services.get(0);
//            ServerInfo service = ServerInfo.newBuilder().setIp(serviceInfo.ip).setPort(serviceInfo.port).build();
//            System.out.println("Returning service with ip: " + serviceInfo.ip + " and port: " + serviceInfo.port);
//            responseObserver.onNext(service);
//            serviceInfo.setClients(serviceInfo.getClients() + 1);
//        }
//        responseObserver.onCompleted();
//    }
//}
