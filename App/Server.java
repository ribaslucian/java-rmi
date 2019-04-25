package App;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
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
    public void addOffer(OfferInterface offer) throws RemoteException {
        ClientInterface client = (ClientInterface) offer.get("client");
        System.out.println(client.getId());

        clients.put(client.getId(), client);
        screen.addOffer(offer);
        notifyIfMatches();
    }

    @Override
    public void cancelOffer(String clientId, Integer offerId) throws RemoteException {
        screen.cancelOffer(clientId, offerId);
    }

    public void notifyIfMatches() {
//        try {
//            clients.get("cliente-1").notifyOffer("Há novas opções disponíveis.");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        
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
                Offer offer = new Offer();
                Integer offerId = (Integer) tableOffers.getValueAt(i, 1);
                String offerHotel = (String) tableOffers.getValueAt(i, 2);
                String offerCity = (String) tableOffers.getValueAt(i, 3);
                Integer offerPeople = (Integer) tableOffers.getValueAt(i, 4);
                String offerDateDeparture = (String) tableOffers.getValueAt(i, 5);
                String offerDateReturn = (String) tableOffers.getValueAt(i, 6);
                String offerLogistic = (String) tableOffers.getValueAt(i, 7);
                Float offerMaxPrice = (Float) tableOffers.getValueAt(i, 8);


                // para cada quarto cadastrado, verificar se ha combinacao com todos os voos
                for (int j = 0; j < tableBedrooms.getRowCount(); j++) {
                    String bedroomCity = (String) tableBedrooms.getValueAt(j, 0);
                    String bedroomHotel = (String) tableBedrooms.getValueAt(j, 1);
                    Float bedroomPrice = (Float) tableBedrooms.getValueAt(j, 2);

                    // verificar se o hotel e cidade do Quarto eh o mesmo do interesse
                    if (bedroomCity.equals(offerCity) && bedroomHotel.equals(offerHotel) && bedroomPrice <= offerMaxPrice) {
                        // clients.get(offer.clientId).notifyOffer("(Anúncio) Temos uma opção que PODE SER de interesse " + offer.id + ". Compre agora!");
                        
                        // verificar se o voo vai para o mesmo destino
                        // e se o preco acumulado nao estora do desejado
                        for (int k = 0; k < tableFlights.getRowCount(); k++) {
                            String flightCity = (String) tableFlights.getValueAt(k, 0);
                            Float flightPrice = (Float) tableFlights.getValueAt(k, 1) + bedroomPrice;
                            
                            if (flightCity.equals(offerCity) && flightPrice <= offerMaxPrice) {
                                // obtendo referencia do cliente
                                ClientInterface client = (ClientInterface) clients.get(tableOffers.getValueAt(i, 1));
                                client.notifyOffer("(Anúncio) Temos uma opção que É de interesse " + offerId + ". Compre agora!");
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
