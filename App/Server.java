package App;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class Server extends UnicastRemoteObject implements ServerInterface {

    public ServerScreen screen;
    private static Registry referenceNamesService;

    // <ClientId, ClientReference>
    private HashMap<String, ClientInterfaceRemote> clients = new HashMap<>();

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

    public void notifyIfMatches() {
        try {
            DefaultTableModel tableOffers = (DefaultTableModel) screen.getTableOffers().getModel();
            DefaultTableModel tableBedrooms = (DefaultTableModel) screen.getTableBedrooms().getModel();
            DefaultTableModel tableFlights = (DefaultTableModel) screen.getTableFlights().getModel();

            // alguma tabela nao possui registro
            if (tableOffers.getRowCount() == 0
                    || tableBedrooms.getRowCount() == 0
                    || tableFlights.getRowCount() == 0) {
                return;
            }

            // percorrer tabela de interesses para verificar se ha conciliacao
            for (int i = 0; i < tableOffers.getRowCount(); i++) {

                // oferta atual
                String clientId = (String) tableOffers.getValueAt(i, 0);
                Integer offerId = (Integer) tableOffers.getValueAt(i, 1);
                String offerCity = (String) tableOffers.getValueAt(i, 2);
                String offerHotel = (String) tableOffers.getValueAt(i, 3);
                Integer offerPeople = (Integer) tableOffers.getValueAt(i, 4);
                String offerDateDeparture = (String) tableOffers.getValueAt(i, 5);
                String offerDateReturn = (String) tableOffers.getValueAt(i, 6);
                Float offerMaxPrice = (Float) tableOffers.getValueAt(i, 7);

                // para cada quarto cadastrado, verificar se ha combinacao com todos os voos
                for (int j = 0; j < tableBedrooms.getRowCount(); j++) {

                    String bedroomCity = (String) tableBedrooms.getValueAt(j, 0);
                    String bedroomHotel = (String) tableBedrooms.getValueAt(j, 1);
                    Float bedroomPrice = (Float) tableBedrooms.getValueAt(j, 2);

                    // verificar se o hotel e cidade do Quarto eh o mesmo do interesse
                    if (bedroomCity.equals(offerCity) && bedroomHotel.equals(offerHotel) && bedroomPrice <= offerMaxPrice) {

                        // verificar se o voo vai para o mesmo destino
                        // e se o preco acumulado nao estora do desejado
                        for (int k = 0; k < tableFlights.getRowCount(); k++) {
                            String flightCity = (String) tableFlights.getValueAt(k, 0);
                            Float flightPrice = (Float) tableFlights.getValueAt(k, 1) + bedroomPrice;
                            String flightDateDeparture = (String) tableFlights.getValueAt(k, 2);
                            String flightDateReturn = (String) tableFlights.getValueAt(k, 3);

                            // verificar se ha voo para a cidade nas datas informadas e se o preco coindide
                            if (flightCity.equals(offerCity) && flightPrice <= offerMaxPrice && flightDateDeparture.equals(offerDateDeparture) && flightDateReturn.equals(offerDateReturn)) {

                                // notificamos o cliente
                                clients.get(clientId).notifyOffer("(Anúncio) Temos uma opção que É de interesse " + offerId + ". Compre agora!");
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

    @Override
    public void addOffer(OfferInterface offer) throws RemoteException {
        ClientInterfaceRemote client = (ClientInterfaceRemote) offer.get("client");
        clients.put((String) offer.get("clientId"), client);
        screen.addOffer(offer);
        notifyIfMatches();
    }

    @Override
    public void cancelOffer(String clientId, Integer offerId) throws RemoteException {
        screen.cancelOffer(clientId, offerId);
    }

    @Override
    public Boolean payOffer(OfferInterface offer) throws RemoteException {

        // oferta atual
        String clientId = (String) offer.get("clientId");
        Integer offerId = (Integer) offer.get("id");
        String offerCity = (String) offer.get("city");
        String offerHotel = (String) offer.get("hotel");
        Integer offerPeople = (Integer) offer.get("people");
        String offerDateDeparture = (String) offer.get("dateDeparture");
        String offerDateReturn = (String) offer.get("dateReturn");
        Float offerMaxPrice = (Float) offer.get("maxPrice");

        try {
            DefaultTableModel tableBedrooms = (DefaultTableModel) screen.getTableBedrooms().getModel();
            DefaultTableModel tableFlights = (DefaultTableModel) screen.getTableFlights().getModel();

            // alguma tabela nao possui registro
            if (tableBedrooms.getRowCount() == 0 || tableFlights.getRowCount() == 0) {
                clients.get(clientId).notifyOffer("(OPS) Não conseguimos encontrar seu pacote, tente novamente mais tarde.");
                return false;
            }

            // para cada quarto cadastrado, verificar se ha combinacao com todos os voos
            for (int j = 0; j < tableBedrooms.getRowCount(); j++) {

                String bedroomCity = (String) tableBedrooms.getValueAt(j, 0);
                String bedroomHotel = (String) tableBedrooms.getValueAt(j, 1);
                Float bedroomPrice = (Float) tableBedrooms.getValueAt(j, 2);

                // verificar se o hotel e cidade do Quarto eh o mesmo do interesse
                if (bedroomCity.equals(offerCity) && bedroomHotel.equals(offerHotel) && bedroomPrice <= offerMaxPrice) {

                    // verificar se o voo vai para o mesmo destino
                    // e se o preco acumulado nao estora do desejado
                    for (int k = 0; k < tableFlights.getRowCount(); k++) {
                        String flightCity = (String) tableFlights.getValueAt(k, 0);
                        Float flightPrice = (Float) tableFlights.getValueAt(k, 1) + bedroomPrice;
                        String flightDateDeparture = (String) tableFlights.getValueAt(k, 2);
                        String flightDateReturn = (String) tableFlights.getValueAt(k, 3);

                        // verificar se ha voo para a cidade nas datas informadas e se o preco coindide
                        if (flightCity.equals(offerCity) && flightPrice <= offerMaxPrice && flightDateDeparture.equals(offerDateDeparture) && flightDateReturn.equals(offerDateReturn)) {

                            tableBedrooms.removeRow(j);
                            tableFlights.removeRow(k);
                            
                            // notificamos o cliente
                            clients.get(clientId).notifyOffer("(COMPRA) Parabéns! Você efetuou a compra do pacote " + offerId + ".");
                            return true;
                        }
                    }

                    continue;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        clients.get(clientId).notifyOffer("(OPS) Não conseguimos encontrar seu pacote, tente novamente mais tarde.");
        return false;
    }

}
