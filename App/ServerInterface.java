package App;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public void addOffer(OfferInterface offerData) throws RemoteException;
    public void cancelOffer(String clientId, Integer offerId) throws RemoteException;

}
