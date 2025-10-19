package serverapp;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class GroupMember {

    private SpreadConnection connection;
    private MessageHandling msgHandling;
    private SpreadGroup group;
    private final String GROUPNAME = "Servers-Spread-group";
    private static final Logger logger = LogManager.getLogger(GroupMember.class);

    public GroupMember(String serverName, String spreadNodeIp, int spreadDaemonNodePort, SpreadGroupManager groupManager, Gson gson){
        //Iniciar ligação Spread
        try {
            logger.info("Initializing Spread connection");
            connection = new SpreadConnection();
            connection.connect(InetAddress.getByName(spreadNodeIp),spreadDaemonNodePort,serverName,false,true);
            logger.info("Spread connection established, readying message handling");
            msgHandling = new MessageHandling(connection,groupManager,gson);
            connection.add(msgHandling);
        }catch(SpreadException e)  {
            System.err.println("There was an error connecting to the daemon.");
            e.printStackTrace();
            System.exit(1);
        }
        catch(UnknownHostException e) {
            System.err.println("Can't find the daemon " + spreadNodeIp);
            System.exit(1);
        }
        try {
            group = new SpreadGroup();
            group.join(connection,GROUPNAME);
        }catch(SpreadException e)  {
            System.err.println("There was an error connecting to the daemon.");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void SendMessage(String message) throws SpreadException {
        SpreadMessage msg = new SpreadMessage();
        //Controlar segurança de envio?
        msg.setSafe();
        msg.addGroup(GROUPNAME);
        msg.setData(message.getBytes(StandardCharsets.UTF_8));//TODO SEND GSON
        connection.multicast(msg);
    }

    public String GetGroupName() {
        return connection.getPrivateGroup().toString();
    }

    //Sem saídas graciosas
}
