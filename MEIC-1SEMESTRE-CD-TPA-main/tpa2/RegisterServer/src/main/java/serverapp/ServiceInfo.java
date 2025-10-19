package serverapp;

public class ServiceInfo implements Comparable<ServiceInfo> {
    public String ip;
    public int port;
    public String svcName;
    private int clients = 0;

    public ServiceInfo(String ip, int port, String svcName) {
        this.ip = ip;
        this.port = port;
        this.svcName = svcName;
    }

    public int getClients() {
        return clients;
    }

    public void incrementClients() {
        clients++;
    }

    @Override
    public int compareTo(ServiceInfo serviceInfo) {
        return Integer.compare(this.clients, serviceInfo.clients);
    }
}
