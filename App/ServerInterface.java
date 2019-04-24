package App;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public void addOffer(ClientInterface client, Integer id, String city, String hotel, float price, String logistic) throws RemoteException;
    public void cancelOffer(String clientId, Integer offerId) throws RemoteException;

}
