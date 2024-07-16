package org.market.DomainLayer.backend;
import org.market.DomainLayer.backend.UserPackage.UserController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Basket {
    private String username;
    private int storeID;
    private Map<Integer, Integer> products; //-> <productId,quantity>
    private Map<Integer, Integer> prodOffer; //-> <productId,price>


    public Basket(String username, int storeID) {
        this.username = username;
        this.storeID = storeID;
        this.products = new ConcurrentHashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public int getStoreID() {
        return storeID;
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }

    public int getQuantity(Integer product) {
        if (products.containsKey(product))
            return products.get(product);
        else
            return -1;
    }

    public String addProduct(Integer product, int quantity) throws Exception {
        if (quantity > 0) {
            products.put(product, quantity);
            UserController.LOGGER.info("product added successfully");
            return "product added successfully";
        } else {
            UserController.LOGGER.severe("invalid quantity");
            throw new Exception("invalid quantity");
        }
    }

    public StringBuilder inspectBasket() {
        StringBuilder output = new StringBuilder();
        output.append("Store ID: ").append(getStoreID()).append("\n");

        if (products.isEmpty()) {
            output.append("  No products in this store.\n");
        } else {
            for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
                Integer productId = entry.getKey();
                Integer quantity = entry.getValue();
                String name = Market.getPC().getProductName(productId);
                output.append("  Product: ").append(name).append(", Quantity: ").append(quantity).append("\n");
            }
        }
        return output;
    }

    public String removeItem(int product) {
        if (products.containsKey(product)) {
            products.remove(product);
            UserController.LOGGER.info("item removed successfully");
            return "item removed successfully";
        }
        UserController.LOGGER.severe("couldn't find item");
        return "couldn't find item";
    }

    public void addOfferPrice(int productId, int price) {
        prodOffer.put(productId,price);
    }

    public int getOfferPrice(int product) {
        return prodOffer.get(product);
    }
}
