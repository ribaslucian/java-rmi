package App;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {

    public String id;
    public ClientScreen screen;
    private static Registry referenceNamesService;
    private Integer offersCount = 0;
    
    protected Client() throws RemoteException {
        super();
    }

    protected Client(String id) throws RemoteException {
        super();
        this.id = id;
        screen = new ClientScreen(this);
    }

    public void addOffer(String city, String hotel, float maxPrice, String logistic) {
        Integer id = offersCount + 1;
        offersCount = offersCount + 1;
        
        Offer o = new Offer(this.id, id, city, hotel, maxPrice, logistic);

        screen.addOffer(o);

        try {
            getServer().addOffer(this, id, city, hotel, maxPrice, logistic);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ServerInterface getServer() {
        try {
            referenceNamesService = LocateRegistry.getRegistry(1099);
            return (ServerInterface) referenceNamesService.lookup("//localhost:1099/Server");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        return null;
    }

    @Override
    public void cancelOffer(Integer id) {
        try {
            screen.cancelOffer(id);
            getServer().cancelOffer(this.id, id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void notifyOffer(String message) throws RemoteException {
        screen.log(message);
    }

    @Override
    public String getId() throws RemoteException {
        return id;
    }

}
