package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.time.LocalDate;

import org.market.DomainLayer.backend.Market;

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
        double offerPrice=Market.getUC().getUser(username).offerPrice(username,this.storeId,price,productId);
        if(offerPrice!=price){
            double offer=Market.getP().reviewOffer(this.storeId,offerPrice,productId);
            if(offer==offerPrice){
                Market.getSC().getStore(storeId).updateProductPrice(username,productId,offerPrice);
                return true;
            }
            else
                return Market.getUC().reviewOffer(offer,this.username);
        }
        else
            return true;
    }

}
