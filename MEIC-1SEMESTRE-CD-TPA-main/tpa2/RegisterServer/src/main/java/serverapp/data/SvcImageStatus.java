package serverapp.data;

import java.util.List;

public class SvcImageStatus {

    private ImageState imageState;

    public ImageState getImageState() {
        return imageState;
    }
    public void setImageState(ImageState imageState) {
        this.imageState = imageState;
    }

    @Override
    public String toString() {
        return "SvcImageStatus{" +
                ", imageState=" + imageState +
                '}';
    }
}