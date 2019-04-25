package App;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OfferInterface extends Remote {
    
    public void put(String key, Object value) throws RemoteException; 
    public Object get(String key) throws RemoteException;
    public String getClientId() throws RemoteException;

}
