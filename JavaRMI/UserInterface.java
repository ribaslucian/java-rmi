package JavaRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserInterface extends Remote {
    
    public String getName() throws RemoteException;
    
}
