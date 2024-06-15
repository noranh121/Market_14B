package DomainLayer.backend.StorePackage.Purchase;

import java.time.LocalDate;

public class OfferMethod extends PurchaseMethodData implements PurchaseMethod {

    public OfferMethod(int quantity, double price, LocalDate date, int atLeast, double weight, double age) {
        super(quantity, price, date, atLeast, weight, age);
    }

    @Override
    public Boolean purchase(int productId, int quantity, double price, double weight, double age) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'purchase'");
    }

}
