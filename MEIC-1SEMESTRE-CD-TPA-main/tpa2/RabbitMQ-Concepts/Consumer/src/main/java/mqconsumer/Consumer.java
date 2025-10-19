package mqconsumer;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;


import java.util.Scanner;

// libs
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;



public class Consumer {

    static String IP_BROKER="localhost";
    static Logger logger=new SimpleLoggerFactory().getLogger("RabbitMQ-Consumer");
     public static void main(String[] args) {
        try {
            if (args.length > 0) {
                IP_BROKER=args[0];
            }
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(IP_BROKER); factory.setPort(5672);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            Scanner scan = new Scanner(System.in);

            // Consumer handler to receive messages with autoAcK
            /////codigo here//////////////
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String recMessage = new String(delivery.getBody(), "UTF-8");
                String routingKey=delivery.getEnvelope().getRoutingKey();
                System.out.println("dMessage Receive:" +consumerTag+":"+ routingKey+":"+recMessage);
            };
            // Consumer handler to receive cancel receiving messages
            CancelCallback cancelCallback=(consumerTag)->{
                System.out.println("CANCEL Received! "+consumerTag);
            };
            //String basicConsume(String queue, boolean autoAck, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException;
           //String consumeTag=channel.basicConsume(readline("Queue name?"), true, deliverCallback, cancelCallback);
           //System.out.println("Consumer Tag:"+consumeTag);
            
			// sem autoAck
            DeliverCallback deliverCallbackWithoutAck = (consumerTag, delivery) -> {
                String recMessage = new String(delivery.getBody(), "UTF-8");
                String routingKey=delivery.getEnvelope().getRoutingKey();
                long deliverTag=delivery.getEnvelope().getDeliveryTag();
                System.out.println(consumerTag+": Message Received:" + routingKey+":"+recMessage);

//                Simular tempo de processamento da mensagem
//				try {
//                    Thread.sleep(2*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                //void basicAck(long deliveryTag, boolean multiple) throws IOException;
                //void basicNack(long deliveryTag, boolean multiple, boolean requeue) throws IOException;
                if (recMessage.equals("nack"))
                    channel.basicNack(deliverTag, false, true);
                else channel.basicAck(deliverTag,false);
                System.out.println("acknowledge of "+recMessage);
            };
            //prefetchCount = 1 setting. This tells RabbitMQ not to give more than one message
            // to a worker at a time. Or, in other words, don't dispatch a new message to a worker
            // until it has processed and acknowledged the previous one. Instead, it will dispatch
            // it to the next worker that is not still busy.
            //channel.basicQos(1); // definir nº de mensagens posíveis de entregar ao mesmo consumer
            String consumerTag=channel.basicConsume(readline("Queue name?"), false, deliverCallbackWithoutAck, cancelCallback);
            
			System.out.println(consumerTag+": waiting for messages or Press any key to finish");
            scan.nextLine();
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
