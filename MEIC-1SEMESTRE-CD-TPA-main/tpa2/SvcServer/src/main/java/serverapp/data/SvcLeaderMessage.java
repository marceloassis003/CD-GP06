package serverapp.data;

import java.util.Hashtable;
import java.util.List;

public class SvcLeaderMessage {
    private final String svcLeaderName;
    private final Hashtable<String,SvcImageStatus> svcImageStatusList;
    private final Hashtable<String,Integer> svcServerCount;


    public SvcLeaderMessage(String svcLeaderName, Hashtable<String, SvcImageStatus> svcImageStatusList, Hashtable<String, Integer> svcServerCount) {
        this.svcLeaderName = svcLeaderName;
        this.svcImageStatusList = svcImageStatusList;
        this.svcServerCount = svcServerCount;
    }
    public String getSvcLeaderName() {
        return svcLeaderName;
    }
    public Hashtable<String, Integer> getSvcServerCount() {
        return svcServerCount;
    }
    public Hashtable<String,SvcImageStatus> getSvcImageStatus(){
        return svcImageStatusList;
    }

    @Override
    public String toString() {
        return "SvcLeaderMessage{" +
                "svcLeaderName='" + svcLeaderName + '\'' +
                ", svcImageStatusList=" + svcImageStatusList +
                ", svcServerCount=" + svcServerCount +
                '}';
    }
}
