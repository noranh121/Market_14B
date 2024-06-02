package DomainLayer.backend.UserPackage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import DomainLayer.backend.Basket;
import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.StorePackage.Store;
import DomainLayer.backend.StorePackage.StoreController;

public class ShoppingCart {
    private List<Basket> baskets;
    public ShoppingCart() {
        baskets = new LinkedList<>();
    }

    public String addBasket(Basket basket) throws Exception {
        if(basket!=null) {
            baskets.add(basket);
            UserController.LOGGER.info("Basket added successfully");
            return "Basket added successfully";
        }else{
            UserController.LOGGER.severe("Basket is null");
            throw new Exception("Basket is null");
        }
    }

    public List<Basket> getBaskets() {
        return baskets;
    }


    public void addToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        Basket basket = getBasket(username,storeId);
        basket.addProduct(product,quantity);
        UserController.LOGGER.info("added to cart");
    }

    public Basket getBasket(String username, int storeId) {
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

    public String removeCartItem(int storeId, int product) {
        for (Basket basket : baskets) {
            if (basket.getStoreID() == storeId) {
                return basket.removeItem(product);
            }
        }
        UserController.LOGGER.severe("no such item");
        return "no such item";
    }

    public double Buy() throws Exception {
        double sum = 0;
        for (Basket basket : baskets) {
            Store store = StoreController.getInstance().getStore(basket.getStoreID());
            if (store.check(basket.getProducts())) { //policies
                for (Map.Entry<Integer,Integer> entry : basket.getProducts().entrySet()) {
                      double price = store.getProdPrice(entry.getKey()); //discounts
                      sum += price * entry.getValue();
                    UserController.LOGGER.info("Your purchase was successful");
                }
            }
            UserController.LOGGER.severe("invalid cart");
            throw new Exception("invalid cart");
        }
        return sum;
    }
}
