package mqconsumer;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import mqconsumer.data.SvcImageStatus;
import mqconsumer.data.SvcSpreadMessage;
import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class worker {


    private static String exchange_ip = "localhost";
    private static int exchange_port = 5672;
    private static String spread_ip = "35.204.253.159";
    private static int spread_port = 4803;
    private static String SVC_GROUP_NAME = "Servers-Spread-group";
    private static Gson gson = new GsonBuilder().create();
    private static String FACTORY_USERNAME = "guest";
    private static String FACTORY_PASSWORD = "guest";
    private static String filedir = System.getProperty("user.dir");
    private static Logger logger = new SimpleLoggerFactory().getLogger("RabbitMQ-Worker");
    private static GroupMember member;


    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                exchange_ip = args[0];
                exchange_port = Integer.parseInt(args[1]);
                spread_ip = args[2];
                spread_port = Integer.parseInt(args[3]);
                filedir = args[4];
            }
            // Configurar conexão com RabbitMQ
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(exchange_ip);
            factory.setPort(exchange_port);
            factory.setUsername(FACTORY_USERNAME); // Colocar user e pass
            factory.setPassword(FACTORY_PASSWORD);

            // Realizar a conexão channel
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Fila que o worker irá trabalhar
            String exchangeName = "test_exchange";
            String queueName = "teste_queue";
            channel.exchangeDeclare(exchangeName, "fanout", true); // Garantir que a exchange existe
            channel.queueDeclare(queueName, true, false, false, null); // Garantir que a fila existe
            channel.queueBind(queueName, exchangeName, ""); // Vincular fila à exchange

            //channel.basicQos(1); // Configuração de prefetchCount

            System.out.println("Worker ON, waiting for messages...");
            String workerName = "worker" + UUID.randomUUID();
            member = new GroupMember(workerName, spread_ip, spread_port);

            // Configurar callback para processar mensagens
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8"); //serializando message
                long deliveryTag = delivery.getEnvelope().getDeliveryTag(); // Define o delivery
                System.out.println("Message received: " + message);

                try {
                    processMessage(message); // Processar a mensagem
                    channel.basicAck(deliveryTag, false); // Acknowledge após processamento // confirma rececao
                } catch (Exception e) {
                    logger.info("Catch exception");
                    e.printStackTrace();
                    channel.basicNack(deliveryTag, false, true); // Nack se falhar  // rejeita a entrega
                    channel.abort();
                }
            };

            // Consumir mensagens/itens da queue
            channel.basicConsume(queueName, false, deliverCallback, (consumerTag) -> {
                System.out.println("Consumer canceled: " + consumerTag);
            });    // consumir da queue true , parametro autoAck

            // Manter o Worker ativo até o encerramento
            new Scanner(System.in).nextLine();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Função para processar a mensagem
    private static void processMessage(String message) throws Exception {// usando Gson

        // Desserializar a mensagem
        //ImageTaskTag task = gson.fromJson(message, ImageTaskTag.class); // v1 done teste
        SvcImageStatus task = gson.fromJson(message, SvcImageStatus.class);


        // conversão image
        if (task.getKeywords() != null && task.getKeywords().isEmpty()) { //.length == 1
            //task.setKeywords(task.getKeywords().get(0).split(","));
            //String singleKeyword = task.getKeywords()[0];
            //if (singleKeyword.contains(",")) {
              //  task.setKeywords(singleKeyword.split(","));
            //}
            throw new RuntimeException("Invalid keyword list");
        }
        // extração message dados
        String filename = task.getOriginalImageName();
        String[] Keywords = task.getKeywords().toArray(new String[0]);

//        // path para testes
//        String sharedPath = System.getenv("SHARED_PATH");
//        if (sharedPath == null || sharedPath.isEmpty()) {
//            sharedPath = "C:/isel_cd/";
//        }
//        if (!sharedPath.endsWith("/")) {
//            sharedPath += "/";
//        }
        //String sharedPath = "C:/isel_cd/"; //"/mnt/shared/";
        //String inputPath = sharedPath + filename;
        //String outputPath = sharedPath + filename.replace(".jpg", "-marks.jpg");

        // caminho ficheiro de input e output e mark to tag
        File file = new File(filedir,filename);
        String inputPath = file.getAbsolutePath();
        System.out.println("Reading from: " + inputPath);
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        String outputImageName;
        switch (getFileExtension(filename)) {
            case "jpg":
                outputImageName = filename.replace(".jpg", "-marks.jpg");
                break;
            case "png":
                outputImageName = filename.replace(".png", "-marks.png");
                break;
            case "jpeg":
                outputImageName = filename.replace(".jpeg", "-marks.jpeg");
                break;
            default:
                outputImageName = filename; // Sem alterações para outras extensões
                break;
        }
        String outputPath = new File(filedir , outputImageName).getAbsolutePath();
        System.out.println("Writing to : " + outputPath);

        // argumentos preenchimento MarkApp
        String[] markAppArgs = new String[2 + Keywords.length];
        markAppArgs[0] = inputPath;
        markAppArgs[1] = outputPath;
        System.arraycopy(Keywords, 0, markAppArgs, 2, Keywords.length);

        // registar a imagem e add tag
        MarkApp.main(markAppArgs);
        task.setMarkedImageName(outputImageName);
        task.setImageState(ImageState.TAGGED);
        SvcSpreadMessage svcSpreadMessage = new SvcSpreadMessage();
        svcSpreadMessage.setImageStatus(task);
        System.out.println("Sending svc info: " + svcSpreadMessage);
        //logs control
        System.out.println("File processed and saved: " + outputPath);
        String jsonString = gson.toJson(svcSpreadMessage);

        member.SendMessage(SVC_GROUP_NAME,jsonString);
        // envio para o grupo spread
        //sendToSpreadGroup(message, outputPath);

    }
    private static String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        }
        return ""; // Sem extensão
    }
    // send to message para o grupo
//    private static void sendToSpreadGroup(String groupName, String outputPath) {
//        try {
//            SpreadConnection connection = new SpreadConnection();
//            connection.connect(InetAddress.getByName("parametro"), //port, "parametro", false, true);
//
//            GroupMember member = new GroupMember("parametro", "parametro", 4803);
//
//            member.JoinToGrupo(groupName);
//            System.out.println("Joined group: " + groupName);
//
//            member.SendMessage(groupName, outputPath);
//            System.out.println("Sent message to group: " + groupName + " -> " + outputPath);
//
//            member.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
