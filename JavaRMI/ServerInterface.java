package JavaRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public String helloTo(String name) throws RemoteException;
    public String getMyName(String id) throws RemoteException;

}
