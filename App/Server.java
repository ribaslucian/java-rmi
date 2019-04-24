package App;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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

    public static Server start() {
        try {
            Server server = new Server();
            referenceNamesService = LocateRegistry.createRegistry(1099);
            referenceNamesService.bind("//localhost:1099/Server", server);
            System.err.println("Server ready");
            return server;
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addOffer(ClientInterface client, Integer id, String city, String hotel, float price, String logistic) throws RemoteException {
        Offer o = new Offer(client.getId(), id, city, hotel, price, logistic);
        clients.put(client.getId(), client);
        screen.addOffer(o);
        notifyIfMatches();
    }

    @Override
    public void cancelOffer(String clientId, Integer offerId) throws RemoteException {
        screen.cancelOffer(clientId, offerId);
    }

    public void notifyIfMatches() {
        try {
            DefaultTableModel tableOffers = (DefaultTableModel) screen.getTableOffers().getModel();
            DefaultTableModel tableBedrooms = (DefaultTableModel) screen.getTableBedrooms().getModel();
            DefaultTableModel tableFlights = (DefaultTableModel) screen.getTableFlights().getModel();

            // informar todos os clientes que ha novos produtos
            // for (String id : clients.keySet())
            //     clients.get(id).notifyOffer("Há novas opções disponíveis.");

            // alguma tabela nao possui registro
            if (tableOffers.getRowCount() == 0
                    || tableBedrooms.getRowCount() == 0
                    || tableFlights.getRowCount() == 0) {
                return;
            }

            // percorrer tabela de interesses para verificar se ha conciliacao
            for (int i = 0; i < tableOffers.getRowCount(); i++) {
                // oferta atual
                Offer offer = new Offer(
                        (String) tableOffers.getValueAt(i, 0),
                        (Integer) tableOffers.getValueAt(i, 1),
                        (String) tableOffers.getValueAt(i, 2),
                        (String) tableOffers.getValueAt(i, 3),
                        (Float) tableOffers.getValueAt(i, 4),
                        (String) tableOffers.getValueAt(i, 5)
                );

                // para cada quarto cadastrado, verificar se ha combinacao com todos os voos
                for (int j = 0; j < tableBedrooms.getRowCount(); j++) {
                    String bedroomCity = (String) tableBedrooms.getValueAt(j, 0);
                    String bedroomHotel = (String) tableBedrooms.getValueAt(j, 1);
                    Float bedroomPrice = (Float) tableBedrooms.getValueAt(j, 2);

                    // verificar se o hotel e cidade do Quarto eh o mesmo do interesse
                    if (bedroomCity.equals(offer.city) && bedroomHotel.equals(offer.hotel) && bedroomPrice <= offer.price) {
                        // clients.get(offer.clientId).notifyOffer("(Anúncio) Temos uma opção que PODE SER de interesse " + offer.id + ". Compre agora!");
                        
                        // verificar se o voo vai para o mesmo destino
                        // e se o preco acumulado nao estora do desejado
                        for (int k = 0; k < tableFlights.getRowCount(); k++) {
                            String flightCity = (String) tableFlights.getValueAt(k, 0);
                            Float flightPrice = (Float) tableFlights.getValueAt(k, 1) + bedroomPrice;
                            
                            if (flightCity.equals(offer.city) && flightPrice <= offer.price) {
                                clients.get(offer.clientId).notifyOffer("(Anúncio) Temos uma opção que É de interesse " + offer.id + ". Compre agora!");
                                break;
                            }
                        }

                        continue;
                    }
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
