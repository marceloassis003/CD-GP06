package mqconsumer;

public class ImageTaskTag {
    private String fileName;
    private String[] keywords;

    public ImageTaskTag() {}

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        StringBuilder keywordList = new StringBuilder("[");
        boolean first = true;
        for (String keyword : getKeywords()) {
            keywordList.append(first ? "\"" + keyword + "\"" : ", \"" + keyword + "\"");
            first = false;
        }
        keywordList.append("]");
        return "ImageProcessingTask(fileName=" + getFileName() + ", keywords=" + keywordList + ")";
    }
}

