package DomainLayer.backend.UserPackage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import DomainLayer.backend.Basket;
import DomainLayer.backend.ProductPackage.Product;

public class ShoppingCart {
    private static final Logger LOGGER = Logger.getLogger(ShoppingCart.class.getName());
    private String username;
    List<Basket> baskets;
    public ShoppingCart(String username) {
        baskets = new LinkedList<>();
        this.username = username;
    }

    public String addBasket(Basket basket) throws Exception {
        if(basket!=null) {
            baskets.add(basket);
            LOGGER.info("Basket added successfully");
            return "Basket added successfully";
        }else{
            LOGGER.severe("Basket is null");
            throw new Exception("Basket is null");
        }
    }

    public List<Basket> getBaskets() {
        return baskets;
    }


    public void addToCart(Product product, int storeId, int quantity) throws Exception {
        Basket basket = getBasket(storeId);
        basket.addProduct(product,quantity);
        LOGGER.info("added to cart");
    }

    public Basket getBasket(int storeId) {
        for (Basket basket : baskets) {
            if (basket.getStoreID() == storeId) {
                return basket;
            }
        }
        Basket b = new Basket(username,storeId);
        baskets.add(b);
        return b;
    }

    public String inspectCart() {
        StringBuilder output = new StringBuilder();
        if (baskets.isEmpty()) {
            return "Your shopping cart is empty.";
        }
        for (Basket basket : baskets) {
            output.append(basket.inspectBasket());
        }
        return output.toString();
    }

    public String removeCartItem(int storeId, Product product) {
        for (Basket basket : baskets) {
            if (basket.getStoreID() == storeId) {
                return basket.removeItem(product);
            }
        }
        LOGGER.severe("no such item");
        return "no such item";
    }
}
