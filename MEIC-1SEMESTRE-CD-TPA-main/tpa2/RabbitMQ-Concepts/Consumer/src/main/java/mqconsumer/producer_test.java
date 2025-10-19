package mqconsumer;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import mqconsumer.data.SvcImageStatus;

import java.util.List;

public class producer_test {
    public static void main(String[] args) {
        try {
            // Configurar conexão com RabbitMQ
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");

            try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

                // Nome do exchange em formacto fanout
                String exchangeName = "test_exchange";

                // Criar mensagem dummy // implement logical multicast
                //ImageTaskTag task = new ImageTaskTag(); //v1 test done
                SvcImageStatus task = new SvcImageStatus();
                // task.setFileName("photo.jpg"); // v1 done
                task.setOriginalImageName("image-7e1a13bc-d6a8-4a84-b3bc-7f4ae5b779fc.jpg");
                task.setKeywords(List.of(new String[]{"Vista", "Do", "ISEL: Edificil F, XPTO"})); // Array de palavras

                // definindo gson
                Gson gson = new Gson();
                String message = gson.toJson(task);

                // Publicar message no exchange que envia para queue serializada
                channel.basicPublish(exchangeName, "", null, message.getBytes("UTF-8"));
                System.out.println("Message sent: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

