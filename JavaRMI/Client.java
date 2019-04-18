package JavaRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {

    protected Client() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            Registry referenciaServicoNomes = LocateRegistry.getRegistry(1099);
            ServerInterface server = (ServerInterface) referenciaServicoNomes.lookup("//localhost:1099/MyServer");

            User me = new User("Lucian");
            referenciaServicoNomes.bind("//localhost:1099/Me", me);
            
            UserInterface u1 = (UserInterface) referenciaServicoNomes.lookup("//localhost:1099/User1");
            UserInterface u2 = (UserInterface) referenciaServicoNomes.lookup("//localhost:1099/User2");
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
