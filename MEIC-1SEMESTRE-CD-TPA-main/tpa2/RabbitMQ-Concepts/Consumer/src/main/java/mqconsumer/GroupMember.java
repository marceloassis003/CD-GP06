package mqconsumer;

import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMember {

    private SpreadConnection connection;

    private final Map<String, SpreadGroup> groupsBelonging=new HashMap<String,SpreadGroup>();

    public List<String> getNamesOfBelongingGroups()  {
        List<String> groupNames=new ArrayList<String>();
        for (String gn : groupsBelonging.keySet() )
            groupNames.add(gn);
        return groupNames;
    }

    private MessageHandling msgHandling;
    //private AdvancedMessageHandling advancedMsgHandling;
    public GroupMember(String user, String address, int port) {
        // Establish the spread connection.
        try  {
            connection = new SpreadConnection();
            connection.connect(InetAddress.getByName(address), port, user, false, true);

            //msgHandling =new MessageHandling(connection); connection.add(msgHandling);
            //advancedMsgHandling=new AdvancedMessageHandling(connection); connection.add(advancedMsgHandling);
        }
        catch(SpreadException e)  {
            System.err.println("There was an error connecting to the daemon.");
            e.printStackTrace();
            System.exit(1);
        }
        catch(UnknownHostException e) {
            System.err.println("Can't find the daemon " + address);
            System.exit(1);
        }
    }

//    public void JoinToGrupo(String groupName) throws SpreadException {
//        SpreadGroup newGroup=new SpreadGroup();
//        newGroup.join(connection, groupName);
//        groupsBelonging.put(groupName,newGroup);
//    }

    public void SendMessage(String groupToSend, String txtMessage) throws SpreadException {
        SpreadMessage msg = new SpreadMessage();
        msg.setSafe();
        msg.addGroup(groupToSend);
        msg.setData(txtMessage.getBytes(StandardCharsets.UTF_8));
        connection.multicast(msg);
    }

//    public void groupLeave(String nameToLeave) throws SpreadException {
//        SpreadGroup group=groupsBelonging.get(nameToLeave);
//        if(group != null) {
//            group.leave();
//            groupsBelonging.remove(nameToLeave);
//            System.out.println("Left from " + group + ".");
//        } else  { System.out.println("No group to leave."); }
//    }

    public void close() throws SpreadException {
        // remove listener
        connection.remove(msgHandling);
        //connection.remove(advancedMsgHandling);
        // Disconnect.
        connection.disconnect();
    }
}
