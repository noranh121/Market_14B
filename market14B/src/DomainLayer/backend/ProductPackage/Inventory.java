package DomainLayer.backend.ProductPackage;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    public static final int QUANTITY = 0;
    public static final int PRICE = 1;
    private Map<Integer, double[]> products; //prodiD ==> {quant,price}

    public Inventory() {
        this.products = new HashMap<>();
    }

    public void AddProduct(int productId,Double price,int quantity) {
        products.put(productId, new double[] {quantity,price});
    }

    public void RemoveProduct(int productId) {
        products.remove(productId);
    }

//   public String EditProductName(int productId,int storeId,String newName) {
//
//    }

    public void EditProductPrice(int productId,Double newPrice) {
        double[] details = products.get(productId);
        if(details != null){
            details[PRICE] = newPrice;
        }
    }

    public void EditProductQuantity(int productId,int newQuantity) {
        double[] details = products.get(productId);
        if(details != null){
            details[QUANTITY] = newQuantity;
        }

    }
}
