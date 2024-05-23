package DomainLayer.backend;

import DomainLayer.backend.ProductPackage.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Basket {
    private static final Logger LOGGER = Logger.getLogger(Basket.class.getName());
    private String username;
    private int storeID;
    private Map<Product, Integer> products;

    public Basket(String username, int storeID) {
        this.username = username;
        this.storeID = storeID;
        this.products = new HashMap<Product, Integer>();
    }

    public String geUsername() {
        return username;
    }

    public int getStoreID() {
        return storeID;
    }

    public String addProduct(Product product, int quantity) throws Exception {
        if (!products.containsKey(product)) {
            if (quantity > 0) {
                products.put(product, quantity);
                LOGGER.info("product added successfully");
                return "product added successfully";
            } else {
                LOGGER.severe("invalid quantity");
                throw new Exception("invalid quantity");
            }
        } else {
            LOGGER.severe(product + " does not exist");
            throw new Exception(product + " does not exist");
        }
    }

}
