package mqproducer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;

public class ReturnedMessage implements ReturnListener {
    @Override
    public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] body) throws IOException {
        System.out.println("reply code="+replyCode+" rplyText="+replyText+" exchange="+exchange+" routing key="+routingKey+" msg="+new String(body,"UTF-8"));
    }
}
