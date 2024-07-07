package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.time.LocalDate;

import org.market.DomainLayer.backend.Permissions;
import org.market.DomainLayer.backend.UserPackage.UserController;

public class OfferMethod extends PurchaseMethodData implements PurchaseMethod {

    private int storeId;
    private String username;

    public OfferMethod(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int storeId,String username) {
        super(quantity, price, date, atLeast, weight, age);
        this.storeId=storeId;
        this.username=username;
    }

    @Override
    public Boolean purchase(int productId, int quantity, double price, double weight, double age) throws Exception {
        if(this.price!=price){
            double offer=Permissions.getInstance().reviewOffer(this.storeId,this.price,productId);
            if(offer==this.price)
                return true;
            else
                return UserController.getInstance().reviewOffer(offer,this.username);
        }
        else
            return true;
    }

}
