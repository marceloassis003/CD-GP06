package serverapp.data;

public class SvcSpreadMessage {
    SvcInfo svcInfo;
    SvcImageStatus imageStatus;
    SvcLeaderMessage leaderMessage;

    public SvcSpreadMessage() {}

    public SvcInfo getSvcInfo() {
        return svcInfo;
    }
    public void setSvcInfo(SvcInfo svcInfo) {
        this.svcInfo = svcInfo;
    }
    public boolean isSvcInfo() {
        return svcInfo != null;
    }
    public SvcImageStatus getImageStatus() {
        return imageStatus;
    }
    public void setImageStatus(SvcImageStatus imageStatus) {
        this.imageStatus = imageStatus;
    }
    public boolean isImageStatus() {
        return imageStatus != null;
    }
    public SvcLeaderMessage getLeaderMessage() {
        return leaderMessage;
    }
    public void setLeaderMessage(SvcLeaderMessage leaderMessage) {
        this.leaderMessage = leaderMessage;
    }
    public boolean isLeaderMessage() {
        return leaderMessage != null;
    }

    @Override
    public String toString() {
        return "SvcSpreadMessage{" +
                "svcInfo=" + svcInfo +
                ", imageStatus=" + imageStatus +
                ", leaderMessage=" + leaderMessage +
                '}';
    }
}
