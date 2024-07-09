package org.market.DomainLayer.backend.ProductPackage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.market.DataAccessLayer.DataController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("BackendProductController")
public class ProductController {
    private static final Logger LOGGER = Logger.getLogger(ProductController.class.getName());

    private static ProductController instance;
//    @Autowired
//    private DataController dataController;

    private Map<Integer, Product> products;
    private int idCounter;

    public static synchronized ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }

    private ProductController() {
        products = new ConcurrentHashMap<>();
    }

    public synchronized String addProduct(String name, Category category, String description, String brand,double weight) throws Exception {
        for(Product product : products.values()){
            if(product.getName().equals(name)){
                LOGGER.severe("product already exits in the system");
                throw new Exception("product already exits in the system");
            }
        }
        Product prod = new Product(name, description, brand, category,weight);
        int id = idCounter++;
        prod.setId(id);
        category.addProduct(prod.getId());
        products.put(prod.getId(), prod);
        //dataController.initProduct(name, id,category.getId(), description, brand, weight);
        LOGGER.info("Product of ID " + prod.getId() + " ,Name: " + prod.getName() + " Added succeffuly to the system");
        return "Product of ID " + prod.getId() + " ,Name: " + prod.getName() + " Added succeffuly to the system";
    }

    // private boolean contains(String name, Category category) {
        
    // }

    public synchronized String removeProduct(int productID) throws Exception {
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
            LOGGER.severe("product " + productID + " does not exist");
            throw new Exception("product " + productID + " does not exist");
        }
    }

    public int getCatagory(int productID) throws Exception {
        if (products.containsKey(productID)) {
            return products.get(productID).getCategory().getId();
        }
        else {
            LOGGER.severe("product " + productID + " does not exist");
            throw new Exception("product " + productID + " does not exist");
        }
    }

    public String getProductName(int productId) {
        return products.get(productId).getName();
    }

    public int getProductCategory(int productId) {
        return products.get(productId).getCategory().getId();
    }

    public Collection<Product> getProducts(){
        //needs to connect to data, getprod(id){checks map then gets from database if needed}
        return this.products.values();
    }

    public List<Product> getProductsByIDs(List<Integer> ids){
        return ids.stream()
                .map(products::get)
                .filter(product -> product != null)
                .collect(Collectors.toList());
    }

    public Product getProductbyID(int prodId){
        //should check if exists and if not check if exists in database and retrive it
        return products.get(prodId);
    }

}
