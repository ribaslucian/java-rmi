package JavaRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInterface {

    private static Registry referenceNamesService;

    protected Server() throws RemoteException {
        super();
    }

    @Override
    public String helloTo(String name) throws RemoteException {
        System.err.println(name + " is trying to contact!");
        return "Server says hello to " + name;
    }

    @Override
    public String getMyName(String id) throws RemoteException {
        ClientInterface u2;

        try {
            u2 = (ClientInterface) referenceNamesService.lookup("//localhost:1099/" + id);
            return u2.getName();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static void main(String[] args) {
        try {
            referenceNamesService = LocateRegistry.createRegistry(1099);
            referenceNamesService.bind("//localhost:1099/MyServer", new Server());

            Client u1 = new Client("Client 01");
            Client u2 = new Client("Client 02");

            referenceNamesService.bind("//localhost:1099/User1", u1);
            referenceNamesService.bind("//localhost:1099/User2", u2);
            System.err.println("Server ready");

        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }

}
