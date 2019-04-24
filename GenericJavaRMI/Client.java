package GenericJavaRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {

    String name = "Client 0";
    private static Registry referenceNamesService;

    protected Client() throws RemoteException {
        super();
    }

    protected Client(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    public static void main(String[] args) {
        try {
            referenceNamesService = LocateRegistry.getRegistry(1099);
            ServerInterface server = (ServerInterface) referenceNamesService.lookup("//localhost:1099/MyServer");

            Client me = new Client("Lucian");
            referenceNamesService.bind("//localhost:1099/Me", me);

            ClientInterface u1 = (ClientInterface) referenceNamesService.lookup("//localhost:1099/User1");
            ClientInterface u2 = (ClientInterface) referenceNamesService.lookup("//localhost:1099/User2");
            System.out.println(u1.getName());
            System.out.println(u2.getName());

//            String txt = JOptionPane.showInputDialog("What is your name?");
//            String response = server.helloTo(txt);
//            JOptionPane.showMessageDialog(null, response);
            System.out.println(server.getMyName("Me"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
