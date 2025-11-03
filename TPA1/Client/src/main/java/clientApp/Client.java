package clientApp;

import ClientToManager.*;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Client {

    private static String managerIP = "localhost"; //"localhost";  // IP do Manager Server
    //private static String managerIP = "35.246.73.129";
    private static int managerPort = 50051;         // Porta do Manager Server
    private static ManagedChannel managerChannel;
    private static ManagerServiceGrpc.ManagerServiceBlockingStub managerStub;

    public static void main(String[] args) {
        try {

            // parte para teste direto
            String serverIP = "localhost";
            int serverPort = 50051;

            /*if (args.length == 2) {
                managerIP = args[0];
                managerPort = Integer.parseInt(args[1]);
            }
            System.out.println("Connect to " + managerIP + ":" + managerPort + "...");
            managerChannel = ManagedChannelBuilder.forAddress(managerIP, managerPort)
                    .usePlaintext()
                    .build();
            managerStub = ManagerServiceGrpc.newBlockingStub(managerChannel);

            ImgServInfo serverInfo = managerStub.getServer(Empty.getDefaultInstance());
            System.out.println("Server:");
            System.out.println("UID: " + serverInfo.getUid());
            System.out.println("Address: " + serverInfo.getServerInfo().getIp() + ":" + serverInfo.getServerInfo().getPort());
             */
            System.out.println("Connecting directly to ImageServer " + serverIP + ":" + serverPort);

            ClientHandler handler = new ClientHandler(
                    // para teste direto
                    serverIP,
                    serverPort

                    //serverInfo.getServerInfo().getIp(),
                    //serverInfo.getServerInfo().getPort()
            );

            //managerChannel.shutdown().awaitTermination(3, TimeUnit.SECONDS);
            //Thread.sleep(200);

            while (true) {
                switch (menu()) {
                    case 1:
                        //handler.uploadImage();
                        handler.uploadImageFromPath();
                        break;
                    case 2:
                        handler.downloadImage();
                        break;
                    case 3:
                        handler.showStatus();
                        break;
                    case 99:
                        System.out.println("Shutting down client...");
                        handler.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option.");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int menu() {
        Scanner scan = new Scanner(System.in);
        int op;
        do {
            System.out.println();
            System.out.println("... Menu ...");
            System.out.println("1 - Send image");
            System.out.println("2 - Download image");
            System.out.println("3 - Show status");
            System.out.println("99 - Quit");
            System.out.print("Choose an option: ");
            while (!scan.hasNextInt()) {
                System.out.println("Please insert a valid number.");
                scan.next();
            }
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 4) || op == 99));
        return op;
    }
}