package serverapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverapp.data.SvcImageStatus;
import serverapp.data.SvcLeaderMessage;

import java.util.Hashtable;
import java.util.Map;

public class SpreadGroupManager {

    private String myGroupName;
    private Hashtable<String,SvcImageStatus> imageContainer = new Hashtable<>();
    private Hashtable<String,Integer> serverCount = new Hashtable<>();
    private String leader;
    private static final Logger logger = LogManager.getLogger(MessageHandling.class);

    public SpreadGroupManager() {
    }
    public void setMyGroupName(String name) {
        myGroupName = name;
    }
    public void updateImageState(SvcImageStatus imageStatus,String server) {

        String imageName = imageStatus.getOriginalImageName();
        logger.info("Updating image status for " + imageName);
        imageContainer.put(imageName, imageStatus);
        int currentServerCount = serverCount.getOrDefault(server, 0);
        serverCount.put(server, currentServerCount + 1);

    }
    public SvcImageStatus getImageStatus(String imageName) {
        return imageContainer.get(imageName);
    }
    public String getLeader() {
        return leader;
    }
    public void setLeader(String leader) {
        this.leader = leader;
    }

    public void setImageContainer(Hashtable<String, SvcImageStatus> imageContainer) {
        this.imageContainer = imageContainer;
    }

    public void setServerCount(Hashtable<String, Integer> serverCount) {
        this.serverCount = serverCount;
    }

    public boolean isLeader(String serverName) {
        return serverName.equals(leader);
    }

    public boolean canBeLeader() {
        logger.info("Checking if leader is " + leader);
        if (leader.equals(myGroupName)) {return false;}
        String leader = myGroupName;
        int smallestValue = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> entry : serverCount.entrySet()) {
            if (entry.getValue() < smallestValue) {
                smallestValue = entry.getValue();
                leader = entry.getKey();
            }
        }
        logger.info("Leader is " + leader + "and i am" + myGroupName);
        return leader.equals(myGroupName) ;
    }

    public void setSelfAsLeader() {
        leader = myGroupName;
    }

    public boolean checkSelfIsLeader() {
        return myGroupName.equals(leader);
    }

    public SvcLeaderMessage sendLeaderMessage() {
        return new SvcLeaderMessage(myGroupName, imageContainer,serverCount);
    }

    public void acceptChanges(SvcLeaderMessage leaderMessage) {
        setLeader(leaderMessage.getSvcLeaderName());
        setImageContainer(leaderMessage.getSvcImageStatus());
        setServerCount(leaderMessage.getSvcServerCount());
    }

    public void updateLeaderLeft(String targetServer) {
        serverCount.remove(targetServer);
    }
}
