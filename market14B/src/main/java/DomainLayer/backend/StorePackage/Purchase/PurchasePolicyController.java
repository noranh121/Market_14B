package DomainLayer.backend.StorePackage.Purchase;

import java.util.HashSet;

public abstract class PurchasePolicyController extends CompositePurchasePolicy{

    protected HashSet<CompositePurchasePolicy> compositePurchasePolicies;

    public PurchasePolicyController(int id){
        super(id);
        compositePurchasePolicies=new HashSet<>();
    }

    public void addComposite(CompositePurchasePolicy compositePurchasePolicy){
        compositePurchasePolicies.add(compositePurchasePolicy);
    }

    public void removeComposite(int compositePurchasePolicyId) throws Exception{
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(compositePurchasePolicy.getId()==compositePurchasePolicyId)
                compositePurchasePolicies.remove(compositePurchasePolicy);
        }
        throw new Exception(compositePurchasePolicyId+" is not existed");
    }
}
