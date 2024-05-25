package DomainLayer.backend.ProductPackage;

import DomainLayer.backend.ProductPackage.Category;

public class Product {
    private int id;
    private String name;
    private Category category;
    private String description;
    private String brand;
    private double rating; // 0 - 5


    public Product(String name,String description, String brand,Category category){
        //id
        rating = 0;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
