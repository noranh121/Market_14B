package DomainLayer.backend.ProductPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ProductController {
    private static final Logger LOGGER = Logger.getLogger(ProductController.class.getName());

    private ProductController instance;

    private Map<Integer, Product> products;
    private int idCounter;

    public ProductController getInstance(){
        if(instance == null){
            instance = new ProductController();
        }
        return instance;
    }

    private ProductController(){
        products = new HashMap<>();
    }
    public void addProduct(String name, Category category,String description, String brand){
        Product prod = new Product(name, description, brand, category);
        prod.setId(idCounter++);
        products.put(prod.getId(), prod);
        LOGGER.info("Product of ID " + prod.getId() + " ,Name: " + prod.getName() +" Added succeffuly to the system");
    }

    public void removeProduct(int productID){
        products.remove(productID);
    }


    public String getProductInfo(int productID){
        return products.get(productID).getInfo();
    }


     
}
