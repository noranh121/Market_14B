package org.market.Web.DTOS;

import org.market.DomainLayer.backend.ProductPackage.Product;

public class ProductDTO {

    private int id;
    private String name;
    private Double price;
    private String description;
    private int storeid;

    public ProductDTO(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = -1.0;
        this.description = product.getDescription();

    }

    public ProductDTO(Product product, Double price, int storeid){
        this.id = product.getId();
        this.name = product.getName();
        this.price = price;
        this.description = product.getDescription();
        this.storeid = storeid;
    }

    public ProductDTO(){}

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


}
