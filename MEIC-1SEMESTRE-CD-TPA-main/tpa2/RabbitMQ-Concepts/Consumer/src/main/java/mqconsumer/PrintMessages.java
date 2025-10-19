package mqconsumer;

import spread.MembershipInfo;
import spread.SpreadGroup;
import spread.SpreadMessage;

public class PrintMessages {
    public static void printMembershipInfo(MembershipInfo info)   {
        SpreadGroup group = info.getGroup();

        if(info.isRegularMembership()) {
            SpreadGroup members[] = info.getMembers();
            MembershipInfo.VirtualSynchronySet virtual_synchrony_sets[] = info.getVirtualSynchronySets();
            MembershipInfo.VirtualSynchronySet my_virtual_synchrony_set = info.getMyVirtualSynchronySet();
            System.out.println("REGULAR membership for group " + group +
                    " with " + members.length + " members:");
            for( int i = 0; i < members.length; ++i ) {
                System.out.println("\t" + members[i]);
            }
            //System.out.println("Group ID is " + info.getGroupID());
            System.out.print("\tDue to: ");
            if(info.isCausedByJoin()) {
                System.out.println("JOIN of " + info.getJoined());
            }	else if(info.isCausedByLeave()) {
                System.out.println("LEAVE of " + info.getLeft());
            }	else if(info.isCausedByDisconnect()) {
                System.out.println("DISCONNECT of " + info.getDisconnected());
            } else if(info.isCausedByNetwork()) {
                System.out.println("NETWORK change");
                for( int i = 0 ; i < virtual_synchrony_sets.length ; ++i ) {
                    MembershipInfo.VirtualSynchronySet set = virtual_synchrony_sets[i];
                    SpreadGroup setMembers[] = set.getMembers();
                    System.out.print("\t\t");
                    if( set == my_virtual_synchrony_set ) {
                        System.out.print( "(LOCAL) " );
                    } else {
                        System.out.print( "(OTHER) " );
                    }
                    System.out.println( "Virtual Synchrony Set " + i + " has " +
                            set.getSize() + " members:");
                    for( int j = 0; j < set.getSize(); ++j ) {
                        System.out.println("\t\t\t" + setMembers[j]);
                    }
                }
            }
        } else if(info.isTransition()) {
            System.out.println("TRANSITIONAL membership for group " + group);
        } else if(info.isSelfLeave()) {
            System.out.println("SELF-LEAVE message for group " + group);
        }
    }

    static void MessageDetails(SpreadMessage msg)   {
        try  {
            //System.out.println("Received Message:");
            if(msg.isRegular())   {
                System.out.println("The message is: " + new String(msg.getData()));

//                System.out.print("Received a ");
//                if(msg.isUnreliable())
//                    System.out.print("UNRELIABLE");
//                else if(msg.isReliable())
//                    System.out.print("RELIABLE");
//                else if(msg.isFifo())
//                    System.out.print("FIFO");
//                else if(msg.isCausal())
//                    System.out.print("CAUSAL");
//                else if(msg.isAgreed())
//                    System.out.print("AGREED");
//                else if(msg.isSafe())
//                    System.out.print("SAFE");
//                System.out.println(" message.");
//                System.out.print("Sent by  " + msg.getSender() + ". ");
//                SpreadGroup groups[] = msg.getGroups();
//                System.out.println("to " + groups.length + " groups:");
//                for ( SpreadGroup sp :msg.getGroups() )
//                    System.out.println("   "+sp.toString());
//                byte data[] = msg.getData();
//                //System.out.println("The data is " + data.length + " bytes.");
//                System.out.println("The message is: " + new String(data));
            }
            else if ( msg.isMembership() )
            {
                MembershipInfo info = msg.getMembershipInfo();
                printMembershipInfo(info);
            } else if ( msg.isReject() )  {
                // Received a Reject message
                System.out.print("Received a ");
                if(msg.isUnreliable())
                    System.out.print("UNRELIABLE");
                else if(msg.isReliable())
                    System.out.print("RELIABLE");
                else if(msg.isFifo())
                    System.out.print("FIFO");
                else if(msg.isCausal())
                    System.out.print("CAUSAL");
                else if(msg.isAgreed())
                    System.out.print("AGREED");
                else if(msg.isSafe())
                    System.out.print("SAFE");
                System.out.println(" REJECTED message.");
                System.out.print("Sent by  " + msg.getSender() + " ");
                //System.out.println("Type is " + msg.getType() + ".");
                SpreadGroup groups[] = msg.getGroups();
                System.out.println("to " + groups.length + " groups:");
                for ( SpreadGroup sp :msg.getGroups() )
                    System.out.println("   "+sp.toString());
                byte data[] = msg.getData();

                System.out.println("The message is: " + new String(data));
            } else {
                System.out.println("Message is of unknown type: " + msg.getServiceType() );
            }
        }
        catch(Exception e)  {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
