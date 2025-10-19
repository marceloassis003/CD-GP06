package mqconsumer.data;

// libs
import mqconsumer.ImageState;

import java.util.Arrays;
import java.util.List;

public class SvcImageStatus {
    private String originalImageName;
    private List<String> keywords;
    private String markedImageName;
    private ImageState imageState;

    public String getMarkedImageName() {
        return markedImageName;
    }
    public void setMarkedImageName(String markedImageName) {
        this.markedImageName = markedImageName;
    }
    public String getOriginalImageName() {
        return originalImageName;
    }
    public void setOriginalImageName(String originalImageName) {
        this.originalImageName = originalImageName;
    }
    public ImageState getImageState() {
        return imageState;
    }
    public void setImageState(ImageState imageState) {
        this.imageState = imageState;
    }
    public List<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords =keywords;
    }

    // method to string by consumer / producer communication
    @Override
    public String toString() {
        return "SvcImageStatus{" +
                "originalImageName='" + originalImageName + '\'' +
                ", keywords=" + Arrays.toString(new List[]{keywords}) +
                ", markedImageName='" + markedImageName + '\'' +
                ", imageState=" + imageState +
                '}';
    }
}
