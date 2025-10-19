package serverapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SvcRegisterManager {

    static List<ServiceInfo> services = new ArrayList<>();

    public boolean isEmpty(){
       return services.isEmpty();
    }

    public void addService(ServiceInfo service){
        services.add(service);
    }

    public void incrementClients(String svcName){
        ServiceInfo service = services.stream().filter(s -> s.svcName.equals(svcName)).findFirst().get();
        int index = services.indexOf(service);
        services.get(index).incrementClients();
    }

    public void removeService(String svcName){
        ServiceInfo service = services.stream().filter(s -> s.svcName.equals(svcName)).findFirst().get();
        services.remove(service);
    }

    public int getServiceClients(String svcName){
        ServiceInfo service = services.stream().filter(s -> s.svcName.equals(svcName)).findFirst().get();
        int index = services.indexOf(service);
        return services.get(index).getClients();

    }

    public ServiceInfo getServiceWithLessClients(){
        Collections.sort(services);
        return services.get(0);
    }
}
