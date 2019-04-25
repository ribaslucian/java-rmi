package App;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class Offer extends UnicastRemoteObject implements OfferInterface {

    /**
     * Chaves validas:
     *
     * <ClientInterface> client
     * <ClientId> client.getId()
     * <Integer> id
     * <String> city
     * <String> hotel
     * <Integer> people
     * <String> dateDeparture
     * <String> dateReturn
     * <Float> maxPrice
     */
    public HashMap<String, Object> data = new HashMap<>();

    public Offer(HashMap data) throws RemoteException {
        super();
        this.data = data;
    }

    @Override
    public void put(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public Object get(String key) {
        return data.get(key);
    }

//    @Override
//    public String getClientId() {
//        try {
//            ClientInterfaceRemote client = (ClientInterfaceRemote) data.get("client");
//            return (String) client.getId();
//        } catch (RemoteException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return null;
//    }

    protected Offer() throws RemoteException {
        super();
    }

}
