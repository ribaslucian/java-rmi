package App;

class Offer {
    
    public ClientInterface client;
    public String clientId;
    public Integer id;
    public String city;
    public String hotel;
    public float price;
    public String logistic;

    public Offer(String clientId, Integer id, String city, String hotel, float price, String logistic) {
        this.clientId = clientId;
        this.id = id;
        this.city = city;
        this.hotel = hotel;
        this.price = price;
        this.logistic = logistic;
    }
    
    
    
}
