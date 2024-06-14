package DomainLayer.backend.StorePackage;

import java.util.Map;

public abstract class CompositePurchasePolicy {

    private int id;

    public CompositePurchasePolicy(int id){
        this.id=id;
    }
    public int getId() {
        return id;
    }

    public abstract Boolean purchase(Map<Integer, double[]> products, double age);
}
