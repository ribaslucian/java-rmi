package App;

import GenericJavaRMI.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterfaceRemote extends Remote {

//    public String getId() throws RemoteException;
    public void notifyOffer(String message) throws RemoteException;

}
