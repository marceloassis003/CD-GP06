package serverapp;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverapp.data.ImageState;
import serverapp.data.SvcImageStatus;
import serverapp.data.SvcLeaderMessage;
import serverapp.data.SvcSpreadMessage;
import spread.*;

import java.nio.charset.StandardCharsets;

public class MessageHandling implements AdvancedMessageListener {
    private final SpreadConnection connection;
    private final SpreadGroupManager groupManager;
    private final Gson gson;
    private static final Logger logger = LogManager.getLogger(MessageHandling.class);

    public MessageHandling(SpreadConnection connection, SpreadGroupManager manager, Gson gson) {
        this.connection = connection;
        this.groupManager = manager;
        this.gson = gson;
    }
    //Inter server comunication
    @Override
    public void regularMessageReceived(SpreadMessage spreadMessage) {
        byte[] binData = spreadMessage.getData();
        String newJsonString = new String(binData, StandardCharsets.UTF_8);
        //Assumimos que apenas SvcSpreadMessages são enviados no grupo
        SvcSpreadMessage groupMessage = gson.fromJson(newJsonString,SvcSpreadMessage.class);
        if (groupMessage.isImageStatus()) {
            String serverSender = spreadMessage.getSender().toString();
            logger.info("Received image status, updating: " + groupMessage.getImageStatus());
            groupManager.updateImageState(groupMessage.getImageStatus(),serverSender);
        }
        if (groupMessage.isLeaderMessage()) {
            logger.info("Received leader message, updating: " + groupMessage.getLeaderMessage());
            groupManager.acceptChanges(groupMessage.getLeaderMessage());
        }



    }
    //Spread group management
    @Override
    public void membershipMessageReceived(SpreadMessage spreadMessage) {
        logger.info("Membership message received: " + spreadMessage);
        SpreadGroup myPrivateGroup = connection.getPrivateGroup();
        logger.info("myPrivateGroup=" + myPrivateGroup.toString());
        SpreadGroup senderPrivateGroup = spreadMessage.getSender();
        logger.info("senderPrivateGroup=" + senderPrivateGroup.toString());
        logger.info("MemberShip ThreadID:" + Thread.currentThread().getId());
        MembershipInfo info = spreadMessage.getMembershipInfo();
        if (info.isCausedByJoin()){
            //Verificar se há outros servidores
            SpreadGroup members[] = info.getMembers();
            logger.info("Server joined the group with: "+ members.length);
            if (members.length == 2){//Register é sempre um membro, logo quando o 1º svc se juntao ao servidor, está o Register o próprio svc no grupo
                logger.info("Self proclaiming as leader");
                groupManager.setSelfAsLeader();
            }else {
                //Alguém entrou no grupo, verificar se sou o lider, de forma a informar o estado atual dos pedidos
                if (groupManager.checkSelfIsLeader()){
                    logger.info("Updating current status as leader");
                    //enviar estado atual dos pedidos em multicast pois sou o lider
                    SpreadMessage updateMessage = getSpreadMessage(info);
                    try {
                        connection.multicast(updateMessage);
                    } catch (SpreadException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    logger.info("Do nothing since i am not the leader");
                }
            }
        }
        if (info.isCausedByLeave() || info.isCausedByDisconnect()){
            logger.info("Server left the group");
            String targetServer = info.getLeft().toString();
            if (groupManager.isLeader(targetServer)){
                logger.info("Leader left the group");
                groupManager.updateLeaderLeft(targetServer);
                if (groupManager.canBeLeader()){
                    logger.info("Eligible to be leader, self proclaiming as leader");
                    //se foi o lider a sair do grupo && se eu for o que tiver menos clientes
                    //enviar mensagem a indicar os estados atuais (tornando-se assim o líder)
                    SpreadMessage updateMessage = getSpreadMessage(info);
                    try {
                        connection.multicast(updateMessage);
                    } catch (SpreadException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    logger.info("Not eligible to be the leader");
                }
            }
        }
    }

    private SpreadMessage getSpreadMessage(MembershipInfo info) {
        SvcLeaderMessage leaderMessage = groupManager.sendLeaderMessage();
        SvcSpreadMessage svcSpreadMessage = new SvcSpreadMessage();
        svcSpreadMessage.setLeaderMessage(leaderMessage);
        String json = gson.toJson(svcSpreadMessage);
        SpreadMessage updateMessage = new SpreadMessage();
        updateMessage.setSafe();
        updateMessage.addGroup(info.getGroup());
        updateMessage.setData(json.getBytes(StandardCharsets.UTF_8));
        return updateMessage;
    }
}
