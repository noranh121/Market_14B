package org.market.Web.DTOS;

import org.market.DomainLayer.backend.ProductPackage.Product;

import com.vaadin.flow.component.Component;

public class ProductDTO {

    private int id;
    private String name;
    private Double price;
    private String description;
    private String brand;
    private Double weigth;
    private int storeid;
    private double rating;
    private String category;

    public ProductDTO(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = -1.0;
        this.description = product.getDescription();
        this.rating = product.getRating();
        this.category = "Category";
        if(product.getCategory() != null){
            this.category = product.getCategory().getName();
        }
    }

    public ProductDTO(Product product, Double price, int storeid){
        this.id = product.getId();
        this.name = product.getName();
        this.price = price;
        this.description = product.getDescription();
        this.storeid = storeid;
        this.weigth = product.getWeight();
        this.brand = product.getBrand();
        this.rating = product.getRating();
        this.category = "Category";
        if(product.getCategory() != null){
            this.category = product.getCategory().getName();
        }
    }

    public ProductDTO(){}

    public int getId() {
        return id;
    }
    public double getRating(){
        return this.rating;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStoreid() {
        return storeid;
    }

    public void setStoreid(int storeid) {
        this.storeid = storeid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getWeigth() {
        return weigth;
    }

    public void setWeigth(Double weigth) {
        this.weigth = weigth;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

}
