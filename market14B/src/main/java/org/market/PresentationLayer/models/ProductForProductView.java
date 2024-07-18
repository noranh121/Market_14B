package org.market.PresentationLayer.models;

public class ProductForProductView {

    private String name;
    private String description;
    private double price;


    public ProductForProductView(String name, String description, double price){
        this.name = name;
        this.description =description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
