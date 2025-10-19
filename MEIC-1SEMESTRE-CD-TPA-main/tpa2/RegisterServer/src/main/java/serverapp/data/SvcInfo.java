package serverapp.data;

public class SvcInfo {
    private String svcName;
    private String ip;
    private int port;

    public SvcInfo() {}
    public String getSvcName() {
        return svcName;
    }
    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "SvcInfo{" +
                "svcName='" + svcName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}

