package managersvapp;

/*
import imageToManagerStubs.ManagerRegisInfo;
*/
import io.grpc.ServerBuilder;

import java.util.ArrayList;
import java.util.List;

public class ManagerServer
{
    private static int svcPort = 6000;

    public static void main(String[] args)
    {
        List<ImageContainer> imageServers = new ArrayList <>();

        try
        {
            if (args.length > 0) svcPort = Integer.parseInt(args[0]);

            ImageServerHandler imgSvHandler = new ImageServerHandler(imageServers);
            io.grpc.Server svc = ServerBuilder
                    .forPort(svcPort)
                    .addService(imgSvHandler).addService(new ClientHandler(imageServers))
                    .build();
            svc.start();
            System.out.println("Server started listening on port: " + svcPort);

            svc.awaitTermination();
            svc.shutdown();
            //;
        } catch (Exception ex)
        {
            ex.printStrackTrace();
        }
    }
}
