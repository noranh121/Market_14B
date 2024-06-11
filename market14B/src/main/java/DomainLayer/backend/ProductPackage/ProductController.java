package DomainLayer.backend.ProductPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ProductController {
    private static final Logger LOGGER = Logger.getLogger(ProductController.class.getName());

    private static ProductController instance;

    private Map<Integer, Product> products;
    private int idCounter;

    public static synchronized ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }

    private ProductController() {
        products = new HashMap<>();
    }

    public void addProduct(String name, Category category, String description, String brand) {
        Product prod = new Product(name, description, brand, category);
        // update categories
        prod.setId(idCounter++);
        products.put(prod.getId(), prod);
        LOGGER.info("Product of ID " + prod.getId() + " ,Name: " + prod.getName() + " Added succeffuly to the system");
    }

    public String removeProduct(int productID) throws Exception {
        if (products.containsKey(productID)) {
            products.remove(productID);
            LOGGER.info("product " + productID + " removed");
            return "product " + productID + " removed";
        } else {
            LOGGER.severe("product " + productID + " is not exist");
            throw new Exception("product " + productID + " is not exist");
        }
    }

    public String getProductInfo(int productID) throws Exception {
        if (products.containsKey(productID)) {
            return products.get(productID).getInfo();
        } else {
            LOGGER.severe("product " + productID + " is not exist");
            throw new Exception("product " + productID + " is not exist");
        }
    }

    public String getProductName(int productId) {
        return products.get(productId).getName();
    }

}
