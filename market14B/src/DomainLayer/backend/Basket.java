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
    //TODO
    // if product exists increase quantitiy else add
    // right now it says if contains return does not exist?
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

    public int getQuantity(Product product) {
        if (products.containsKey(product))
            return products.get(product);
        else
            return -1;
    }

    public StringBuilder inspectBasket() {
        StringBuilder output = new StringBuilder();
        output.append("Store ID: ").append(getStoreID()).append("\n");

        if (products.isEmpty()) {
            output.append("  No products in this store.\n");
        } else {
            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                Product product = entry.getKey();
                Integer quantity = entry.getValue();
                //TODO product.getName()
                //output.append("  Product: ").append(product.getName()).append(", Quantity: ").append(quantity).append("\n");
            }
        }
        return output;
    }

    public String removeItem(Product product) {
        if (products.containsKey(product)) {
            products.remove(product);
            LOGGER.info("item removed successfully");
            return "item removed successfully";
        }
        return "couldn't find item";
    }
}
