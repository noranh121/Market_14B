package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.time.LocalDate;

public class ImmediatePurchase extends PurchaseMethodData implements PurchaseMethod {

    public ImmediatePurchase(int quantity, double price, LocalDate date, int atLeast, double weight, double age) {
        super(quantity, price, date, atLeast, weight, age);
    }

    @Override
    public Boolean purchase(int productId, int quantity, double price, double weight, double age) {
        if (atLeast == 0) {
            if ((this.quantity != -1 && this.quantity > quantity) ||
                    (this.price != -1 && this.price > price) ||
                    (this.date != null && this.date.isAfter(LocalDate.now())) ||
                    (this.weight != -1 && this.weight > weight) ||
                    (this.age != -1 && this.age > age)) {
                return false;
            }
        } else if (atLeast == 1) {
            if ((this.quantity != -1 && this.quantity < quantity) ||
                    (this.price != -1 && this.price < price) ||
                    (this.date != null && this.date.isBefore(LocalDate.now())) ||
                    (this.weight != -1 && this.weight < weight) ||
                    (this.age != -1 && this.age < age)) {
                return false;
            }
        }
        return true;
    }

}
