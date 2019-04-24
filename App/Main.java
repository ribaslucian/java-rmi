package App;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        try {
            Client c1 = new Client("cliente-1");
            Server.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
