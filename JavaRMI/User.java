package JavaRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class User extends UnicastRemoteObject implements UserInterface {
    
    String name = "User 0";
    
    protected User() throws RemoteException {
        super();
    }
    
    protected User(String name) throws RemoteException {
        super();
        this.name = name;
    }
    
    

    @Override
    public String getName() throws RemoteException {
        return name;
    }
    
    
    
}
