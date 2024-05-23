package DomainLayer.backend.UserPackage;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import DomainLayer.backend.Basket;

public class ShoppingCart {
    private static final Logger LOGGER = Logger.getLogger(ShoppingCart.class.getName());

    List<Basket> baskets;
    public ShoppingCart() {
        baskets = new LinkedList<>();
    }

    public String addBasket(Basket basket) throws Exception {
        if(basket!=null) {
            baskets.add(basket);
            LOGGER.info("Bascket added successfully");
            return "Bascket added successfully";
        }else{
            LOGGER.severe("Bascket is null");
            throw new Exception("Bascket is null");
        }
    }

    public List<Basket> getBaskets() {
        return baskets;
    }
}
