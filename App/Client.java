package App;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

public class Client extends UnicastRemoteObject implements ClientInterface, ClientInterfaceRemote {

    public String id;
    public ClientScreen screen;
    private static Registry referenceNamesService;
    private Integer offersCount = 0;
    private HashMap<Integer, Offer> offers = new HashMap<>();

    protected Client() throws RemoteException {
        super();
        id = "client-00";
    }

    protected Client(String id) throws RemoteException {
        super();
        this.id = id;
        screen = new ClientScreen(this);
    }

    public void addOffer(Offer offer) {
        try {

            // definindo ID no offerData
            offer.put("id", (offersCount = offersCount + 1));
            offer.put("client", this);
            offer.put("clientId", id);

            offers.put(offersCount, offer);
            screen.addOffer(offer);
            getServer().addOffer(offer);

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
            offers.remove(id);
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

//    @Override
//    public String getId() throws RemoteException {
//        return id;
//    }

    @Override
    public void payOffer(Integer id) {
        try {
            if (getServer().payOffer(offers.get(id))) {
                cancelOffer(id);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
