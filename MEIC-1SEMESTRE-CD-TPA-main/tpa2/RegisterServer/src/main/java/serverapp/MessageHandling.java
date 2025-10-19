package serverapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import serverapp.data.ImageState;
import serverapp.data.SvcSpreadMessage;
import spread.*;

public class MessageHandling implements AdvancedMessageListener {
    private final SpreadConnection connection;
    private final SvcRegisterManager svcRegisterManager;

    public MessageHandling(SpreadConnection connection, SvcRegisterManager svcRegisterManager) {
        this.connection = connection;
        this.svcRegisterManager = svcRegisterManager;
    }

    @Override
    public void regularMessageReceived(SpreadMessage spreadMessage) {

        String svcName = spreadMessage.getSender().toString();
        System.out.println("Received message from : " + svcName);

        Gson js = new GsonBuilder().create();
        String message = new String(spreadMessage.getData());

        SvcSpreadMessage svcMessage = js.fromJson(message, SvcSpreadMessage.class);
        if (svcMessage.isSvcInfo()) {
            ServiceInfo service = new ServiceInfo(svcMessage.getSvcInfo().getIp(), svcMessage.getSvcInfo().getPort(), svcMessage.getSvcInfo().getSvcName());
            svcRegisterManager.addService(service);
            System.out.println("Service added to register: " + svcRegisterManager.getServiceWithLessClients().svcName);
        }

        if (svcMessage.isImageStatus() && (svcMessage.getImageStatus().getImageState() == ImageState.DOWNLOADING || svcMessage.getImageStatus().getImageState() == ImageState.UPLOADING)) {
            svcRegisterManager.incrementClients(svcName);
            System.out.println("Service " + svcName + " has " + svcRegisterManager.getServiceClients(svcName) + " clients");
        }

        System.out.println("Regular ThreadID:" + Thread.currentThread().getId());
        System.out.println("Register Received: " + new String(message));

    }

    @Override
    public void membershipMessageReceived(SpreadMessage spreadMessage) {
        System.out.println("MemberShip ThreadID:" + Thread.currentThread().getId());
        MembershipInfo info = spreadMessage.getMembershipInfo();
        if (info.isSelfLeave() || info.isCausedByDisconnect() ) {
            String svcName = info.getLeft().toString();
            System.out.println("Removing: " + svcName);
            svcRegisterManager.removeService(svcName);
            System.out.println("Service removed from register: " + svcName);
        } else {
            //if (info.getMembers() != null) {
            SpreadGroup[] members = info.getMembers();
            System.out.println("members of belonging group:"+info.getGroup().toString());
            for (int i = 0; i < members.length; ++i) {
                System.out.print(members[i] + ":");
            }
            System.out.println();
        }
    }
}
