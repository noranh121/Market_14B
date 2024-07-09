package org.market.DomainLayer.backend.ProductPackage;

public class Product {
    private int id;
    private String name;
    private Category category;
    private String description;
    private String brand;
    private double weight;

    private double rating; // 0 - 5
    // should be synchronized

    public Product(String name, String description, String brand, Category category,double weight) {
        // id
        rating = 0;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.weight=weight;
    }

    public double getWeight() {
        return weight;
    }

    public synchronized void setWeight(double weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public synchronized void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public synchronized void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public synchronized void setBrand(String brand) {
        this.brand = brand;
    }

    public double getRating() {
        return rating;
    }

    public synchronized void setRating(double rating) {
        this.rating = rating;
    }

    public String getInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Product Information:\n");
        info.append("Name: ").append(name).append("\n");
        info.append("Brand: ").append(brand).append("\n");
        info.append("Category: ").append(category.getName()).append("\n");
        info.append("Rating: ").append(rating);
        info.append("Description: ").append(description).append("\n");
        return info.toString();
    }
}