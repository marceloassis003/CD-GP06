package mqconsumer.data;

public class SvcSpreadMessage {
    SvcImageStatus imageStatus;

    public SvcSpreadMessage() {}

    public SvcImageStatus getImageStatus() {
        return imageStatus;
    }
    public void setImageStatus(SvcImageStatus imageStatus) {
        this.imageStatus = imageStatus;
    }
    public boolean isImageStatus() {
        return imageStatus != null;
    }

    @Override
    public String toString() {
        return "SvcSpreadMessage{" +
                "imageStatus=" + imageStatus +
                '}';
    }
}
