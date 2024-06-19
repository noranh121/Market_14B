package DomainLayer.backend.StorePackage.Purchase;

import java.util.Map;

public class IF_THENPurchaseRule extends PurchasePolicyController{

    public IF_THENPurchaseRule(int id) {
        super(id);
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age) {
        Boolean flag=true;
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(!flag || !compositePurchasePolicy.purchase(products, age))
                return false;
        }
        return true;
    }

}
