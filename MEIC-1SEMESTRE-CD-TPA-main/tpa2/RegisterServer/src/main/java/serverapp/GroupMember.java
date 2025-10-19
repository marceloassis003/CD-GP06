package serverapp;

import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GroupMember {

    private SpreadConnection connection;
    private MessageHandling msgHandling;
    private SpreadGroup group;
    private final String GROUPNAME = "Servers-Spread-group";


    public GroupMember(String serverName, String spreadNodeIp, int spreadDaemonNodePort, SvcRegisterManager svcRegisterManager) {
        //Iniciar ligação Spread
        try {
            connection = new SpreadConnection();
            connection.connect(InetAddress.getByName(spreadNodeIp),spreadDaemonNodePort,serverName,false,true);
            msgHandling = new MessageHandling(connection, svcRegisterManager);
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
        msg.setData(message.getBytes());//TODO SEND GSON
        connection.multicast(msg);
    }

    //Sem saídas graciosas
}
