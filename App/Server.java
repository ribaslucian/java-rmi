package App;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

public class Server extends UnicastRemoteObject implements ServerInterface {

    public ServerScreen screen;
    private static Registry referenceNamesService;

    // <ClientId, ClientReference>
    private HashMap<String, ClientInterface> clients = new HashMap<>();

    protected Server() throws RemoteException {
        super();
        screen = new ServerScreen(this);
        screen.setVisible(true);
    }

    public static void start() {
        try {
            referenceNamesService = LocateRegistry.createRegistry(1099);
            referenceNamesService.bind("//localhost:1099/Server", new Server());
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void addOffer(ClientInterface client, Integer id, String city, String hotel, float price, String logistic) throws RemoteException {
        Offer o = new Offer(client.getId(), id, city, hotel, price, logistic);
        clients.put(client.getId(), client);
        screen.addOffer(o);
    }

    @Override
    public void cancelOffer(String clientId, Integer offerId) throws RemoteException {
        screen.cancelOffer(clientId, offerId);
    }

    public void notifyIfMatches() {
        JTable tableOffers = screen.getTableOffers();
        JTable tableBedrooms = screen.getTableBedrooms();
        JTable tableFlights = screen.getTableFlights();

        // alguma table nao possui registro
        if (tableOffers.getRowCount() == 0
                || tableBedrooms.getRowCount() == 0
                || tableFlights.getRowCount() == 0) {
            return;
        }

        for (String id : clients.keySet()) {
            try {
                clients.get(id).notifyOffer("Há novas opções disponíveis.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
