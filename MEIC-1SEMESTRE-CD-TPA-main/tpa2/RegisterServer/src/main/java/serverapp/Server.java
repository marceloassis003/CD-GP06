package serverapp;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import regStubs.RegisterServiceGrpc;
import regStubs.ServerInfo;

public class Server extends RegisterServiceGrpc.RegisterServiceImplBase {

    private static int svcPort = 8500;
    private static String spreadNodeIp = "34.90.130.219";
    private static int spreadDaemonNodePort = 4803;
    private static String serverName = "register";
//    static List<ServiceInfo> services = new ArrayList<>();
    static SvcRegisterManager svcRegisterManager = new SvcRegisterManager();

    public static void main(String[] args) {

        try {
            if (args.length > 0) {
                svcPort = Integer.parseInt(args[0]);//Porto do servidor SVC
                spreadNodeIp = args[1]; //Ip do Node a ligar
                spreadDaemonNodePort = Integer.parseInt(args[2]); //Porto do Node a ligar
            }

            GroupMember member = new GroupMember(serverName,spreadNodeIp,spreadDaemonNodePort, svcRegisterManager);

            io.grpc.Server svc = ServerBuilder
                    .forPort(svcPort)
                    .addService(new Server())
                    .build();
            svc.start();
            System.out.println("Server started, listening on " + svcPort);

            svc.awaitTermination();
            svc.shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void getService (com.google.protobuf.Empty request, StreamObserver<ServerInfo> responseObserver){
        System.out.println("Returning service info");
        if (svcRegisterManager.isEmpty()){
            System.out.println("No services available");
            responseObserver.onNext(null);
        } else {
            ServiceInfo serviceInfo = svcRegisterManager.getServiceWithLessClients();
            ServerInfo service = ServerInfo.newBuilder().setIp(serviceInfo.ip).setPort(serviceInfo.port).build();
            System.out.println("Returning service with ip: " + serviceInfo.ip + " and port: " + serviceInfo.port);
            responseObserver.onNext(service);
        }
        responseObserver.onCompleted();
    }

}
