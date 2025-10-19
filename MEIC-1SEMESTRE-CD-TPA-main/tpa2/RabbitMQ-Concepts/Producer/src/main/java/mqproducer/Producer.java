package mqproducer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;

import java.util.Scanner;

public class Producer {


    private static String IP_BROKER="localhost";

    private static String exchangeName = null;
    private static String routingKey = null;
    static Logger logger=new SimpleLoggerFactory().getLogger("RabbitMQ-Producer");

    public static void main(String[] args) {

        try {
            if (args.length > 0) {
                IP_BROKER=args[0];
            }
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(IP_BROKER); factory.setPort(5672);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.addReturnListener(new ReturnedMessage());
            // Send message to exchange
            exchangeName = readline("Exchange name?");
            routingKey = readline("Routing key?");
            for (int i = 0; i < 30; i++) {
                String message = "Message #" + i;
                //Thread.sleep(1000);
                channel.basicPublish(exchangeName, routingKey, true, null, message.getBytes());
                System.out.println(" Message Sent:" + message);
            }
            
			readline("Prees enter to finish");
            channel.close();
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String readline(String msg) {
        Scanner scaninput = new Scanner(System.in);
        System.out.println(msg);
        return scaninput.nextLine();
    }

}
