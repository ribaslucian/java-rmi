package JavaRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInterface {
    
    static Registry referenciaServicoNomes;

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
        UserInterface u2;
        
        try {
            u2 = (UserInterface) referenciaServicoNomes.lookup("//localhost:1099/" + id);
            return u2.getName();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }

    public static void main(String[] args) {
        try {
            referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            referenciaServicoNomes.bind("//localhost:1099/MyServer", new Server());

            User u1 = new User("User 01");
            User u2 = new User("User 02");

            referenciaServicoNomes.bind("//localhost:1099/User1", u1);
            referenciaServicoNomes.bind("//localhost:1099/User2", u2);
            System.err.println("Server ready");
            
            
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }

}
