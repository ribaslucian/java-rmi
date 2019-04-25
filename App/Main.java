package App;

public class Main {

    public static void main(String[] args) {
        try {

//            Client c1 = new Client("cliente-1");
//            Client c2 = new Client("cliente-2");

            Server.start();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
