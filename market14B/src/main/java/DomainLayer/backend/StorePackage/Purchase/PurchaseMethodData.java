package DomainLayer.backend.StorePackage.Purchase;

import java.time.LocalDate;

public class PurchaseMethodData {
    protected int quantity; // default -1
    protected double price; // default -1
    protected LocalDate date; // default null
    protected int atLeast; // default -1 --> 0:atleast 1:atmost
    protected double weight; // default -1
    protected double age; // default -1
    protected int storeId;

    public PurchaseMethodData(int quantity, double price, LocalDate date, int atLeast, double weight, double age) {
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.atLeast = atLeast;
        this.weight = weight;
        this.age = age;
    }
}
