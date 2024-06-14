package DomainLayer.backend.StorePackage;

import java.util.HashSet;

public abstract class PurchasePolicyController extends CompositePurchasePolicy{

    protected HashSet<CompositePurchasePolicy> compositePurchasePolicies;

    public PurchasePolicyController(int id){
        super(id);
        compositePurchasePolicies=new HashSet<>();
    }

    public void addComposite(CompositePurchasePolicy compositePurchasePolicy) throws Exception{
        compositePurchasePolicies.add(compositePurchasePolicy);
    }

    public void removeComposite(int compositePurchasePolicyId) throws Exception{
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(compositePurchasePolicy.getId()==compositePurchasePolicyId)
                compositePurchasePolicies.remove(compositePurchasePolicy);
        }
        throw new Exception(compositePurchasePolicyId+"is not existed");
    }
}
