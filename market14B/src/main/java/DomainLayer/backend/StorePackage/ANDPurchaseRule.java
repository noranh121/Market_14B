package DomainLayer.backend.StorePackage;

import java.util.Map;

public class ANDPurchaseRule extends PurchasePolicyController{

    public ANDPurchaseRule(int id) {
        super(id);
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age){
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(!compositePurchasePolicy.purchase(products, age))
                return false;
        }
        return true;
    }

}
