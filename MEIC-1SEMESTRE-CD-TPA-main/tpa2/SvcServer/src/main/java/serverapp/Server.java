package serverapp;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.grpc.ServerBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverapp.data.SvcInfo;
import serverapp.data.SvcSpreadMessage;

import java.util.UUID;


public class Server {

    private static int svcPort = 8501;
    private static String svcIp = "localhost";
    private static String storageDir = System.getProperty("user.dir");
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static String spreadNodeIp = "34.90.130.219";//Node-a
    private static int spreadDaemonNodePort = 4803;
    private static String exchangeIp = "localhost";
    private static int exchangeDaemonNodePort = 5672;

    public static void main(String[] args) {
        logger.info("Starting server");
        try {
            if (args.length > 0) {
                svcIp = args[0];
                svcPort = Integer.parseInt(args[1]);//Porto do servidor SVC
                spreadNodeIp = args[2]; //Ip do Node a ligar
                storageDir = args[3]; //Diretoria onde guardar as imagens
                exchangeIp = args[4];
            }
            logger.info("Storage directory: " + storageDir);
            String unique = UUID.randomUUID().toString().substring(0, 6);
            logger.info(unique);
            String serverName = "Svc-" + unique; //Criar restantes 6 caractéres
            logger.info("Starting server with name: " + serverName);
            logger.info("Connecting do Spread Daemon at {}:{}", spreadNodeIp, spreadDaemonNodePort);
            SpreadGroupManager manager = new SpreadGroupManager();
            Gson gson = new GsonBuilder().create();

            GroupMember member = new GroupMember(serverName,spreadNodeIp,spreadDaemonNodePort,manager,gson);
            String groupName = member.GetGroupName();
            manager.setMyGroupName(groupName);
            logger.info("Joined message");



            //Enviar a mensagem com informações do servidor para a spread Group
            SvcInfo info = new SvcInfo();
            info.setSvcName(groupName);info.setIp(svcIp);info.setPort(svcPort);
            logger.info("Sending my info to spread group:"+ info);
            SvcSpreadMessage message = new SvcSpreadMessage();
            message.setSvcInfo(info);
            String jsonString = gson.toJson(message);
            logger.info(jsonString);
            member.SendMessage(jsonString);
            //joingroup
            //sendMessage

            // Configurar conexão com RabbitMQ
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(exchangeIp);
            factory.setPort(exchangeDaemonNodePort);
            factory.setUsername("guest");
            factory.setPassword("guest");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            io.grpc.Server svc = ServerBuilder
                .forPort(svcPort)
                .addService(new ClientService(storageDir,member,gson,channel,manager))
                .build();
            svc.start();
            System.out.println("Server started, listening on " + svcPort);

            svc.awaitTermination();
            svc.shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
