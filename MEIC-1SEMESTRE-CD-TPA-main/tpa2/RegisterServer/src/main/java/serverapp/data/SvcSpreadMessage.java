package serverapp.data;

public class SvcSpreadMessage {
    SvcInfo svcInfo;
    SvcImageStatus imageStatus;

    public SvcImageStatus getImageStatus() {
        return imageStatus;
    }

    public SvcSpreadMessage() {}

    public boolean isImageStatus() {
        return imageStatus != null;
    }

    public SvcInfo getSvcInfo() {
        return svcInfo;
    }
    public void setSvcInfo(SvcInfo svcInfo) {
        this.svcInfo = svcInfo;
    }
    public boolean isSvcInfo() {
        return svcInfo != null;
    }
}
