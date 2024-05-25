package DomainLayer.backend;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Purchase {
    public static final int QUANTITY = 0;
    public static final int PRICE = 1;
    private static final Logger LOGGER = Logger.getLogger(Purchase.class.getName());
    private Map<Integer, double[]> purchases; //prodid ==> {quantity, price}
    private int storeId;
    private String username;
    private double Ovlprice;
    private int purchaseid;



    public Purchase(Basket basket, double calculatedPrice, Map<Integer,double[]> purchases){
        purchases = new HashMap<>();
        this.username = basket.getUsername(); //method in basket
        this.storeId = basket.getStoreID(); //method in basket
        this.Ovlprice = calculatedPrice; //calculate price in basket!
        this.purchases = purchases;
        LOGGER.info("Purchase captured!");
    }

    public void setID(int purchaseid){
        this.purchaseid = purchaseid;
    }

    public int getID(){
        return this.purchaseid;
    }

    public String FetchInfo(){
        StringBuilder info = new StringBuilder();
        info.append("Username: ").append(username).append("\n");
        info.append("Store ID: ").append(storeId).append("\n");
        info.append("Overall Price: ").append(Ovlprice).append("\n");
        info.append("Purchases:\n");
        for (Map.Entry<Integer, double[]> entry : purchases.entrySet()) {
            int productId = entry.getKey();
            double[] details = entry.getValue();
            //String name; Get product name here
            info.append("Product ID: ").append(productId)
                //.append(", Product Name: ").append(name)
                .append(", Quantity: ").append(details[QUANTITY])
                .append(", Price: ").append(details[PRICE]).append("\n");
        }
        return info.toString();   
    }
}
