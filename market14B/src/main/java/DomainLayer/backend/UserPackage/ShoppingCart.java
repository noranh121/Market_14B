package DomainLayer.backend.UserPackage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import DomainLayer.backend.Basket;
import DomainLayer.backend.Purchase;
import DomainLayer.backend.PurchaseHistory;
import DomainLayer.backend.StorePackage.Store;
import DomainLayer.backend.StorePackage.StoreController;

public class ShoppingCart {
    private List<Basket> baskets;

    public ShoppingCart() {
        baskets = new LinkedList<>();
    }

    public String addBasket(Basket basket) throws Exception {
        if (basket != null) {
            baskets.add(basket);
            UserController.LOGGER.info("Basket added successfully");
            return "Basket added successfully";
        } else {
            UserController.LOGGER.severe("Basket is null");
            throw new Exception("Basket is null");
        }
    }

    public List<Basket> getBaskets() {
        return baskets;
    }

    public void addToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        Basket basket = getBasket(username, storeId);
        basket.addProduct(product, quantity);
        UserController.LOGGER.info("added to cart");
    }

    public Basket getBasket(String username, int storeId) {
        for (Basket basket : baskets) {
            if (basket.getStoreID() == storeId) {
                return basket;
            }
        }
        Basket b = new Basket(username, storeId);
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

    public double Buy(String username) throws Exception {
        double sum = 0;
        StoreController storeController = StoreController.getInstance();
        Store store;

        for (Basket basket : baskets) {
            store = storeController.getStore(basket.getStoreID());
            if (store.check(basket.getProducts())) { // policies
                sum += processBasket(basket, store, username);
            }
            else {
                UserController.LOGGER.severe("invalid cart");
                throw new Exception("invalid cart");
            }
        }
        // process payment
        //if payment unsuccessful {
        // cancelPurchase();
        //}
        UserController.LOGGER.info("Your purchase was successful");
        return sum;
    }

    private double processBasket(Basket basket, Store store, String username) throws Exception {
        double basketSum = 0;
        PurchaseHistory purchaseHistory = PurchaseHistory.getInstance();
        Map<Integer, double[]> purchases = new HashMap<>(); // prodid ==> {quantity, price,weight}
        double[] qp;
        for (Map.Entry<Integer, Integer> entry : basket.getProducts().entrySet()) { // <prod,quan>
            int productId = entry.getKey();
            int quantity = entry.getValue();
            double price = store.getProdPrice(productId);
            double weight=store.getProdWeight(productId);
            // basketSum += price * quantity;
            store.subQuantity(productId, quantity);
            qp = new double[]{quantity,price,weight};
            purchases.put(productId, qp);
        }
        // purchase after that discount
        double age= UserController.getInstance().getUser(username).getAge();
        if(store.purchase(purchases,age)){
            basketSum=store.calculateDiscount(purchases);
            Purchase purchase = new Purchase(basket, basketSum, purchases);
            purchaseHistory.addPurchase(basket.getStoreID(), username, purchase);
        }
        return basketSum;
    }

    private void cancelPurchase() throws Exception {
        StoreController storeController = StoreController.getInstance();
        for (Basket basket : baskets) {
            Store store = storeController.getStore(basket.getStoreID());
            for (Map.Entry<Integer, Integer> entry : basket.getProducts().entrySet()) { // <prod,quan>
                int productId = entry.getKey();
                int quantity = entry.getValue();
                store.addQuantity(productId, quantity);
            }
        }
    }
    
}
