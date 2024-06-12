package DomainLayer.backend.ProductPackage;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    public static final int QUANTITY = 0;
    public static final int PRICE = 1;
    private Map<Integer, double[]> products; // prodiD ==> {quant,price}

    public Inventory() {
        this.products = new HashMap<>();
    }

    public void AddProduct(int productId, Double price, int quantity) {
        products.put(productId, new double[] { quantity, price });
    }

    public void RemoveProduct(int productId) {
        products.remove(productId);
    }

    public double getPrice(int prodID) {
        return (products.containsKey(prodID)) ? products.get(prodID)[PRICE] : -1;
    }

    public int getQuantity(int prodID) {
        return (int) products.get(prodID)[QUANTITY];
    }

    public void EditProductPrice(int productId, Double newPrice) {
        double[] details = products.get(productId);
        if (details != null) {
            details[PRICE] = newPrice;
        }
    }

    public void EditProductQuantity(int productId, int newQuantity) {
        double[] details = products.get(productId);
        if (details != null) {
            details[QUANTITY] = newQuantity;
        }

    }

    public String fetchInfo() {
        StringBuilder description = new StringBuilder();
        for (Map.Entry<Integer, double[]> entry : products.entrySet()) {
            int productId = entry.getKey();
            double[] details = entry.getValue();
            int quantity = (int) details[QUANTITY];
            double price = details[PRICE];
            description.append("Product ID: ").append(productId)
                    .append(", Quantity: ").append(quantity) // out of stock?
                    .append(", Price: $").append(price).append("\n");
        }
        return description.toString();
    }
}