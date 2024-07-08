package org.market.Web.DTOS;

import org.market.DomainLayer.backend.ProductPackage.Product;

public class ProductDTO {

    private int id;
    private String name;
    private Double price;
    private String description;
    private int storeid;

    public ProductDTO(Product product, Double price, int storeid){
        this.id = product.getId();
        this.name = product.getName();
        this.price = price;
        this.description = product.getDescription();
        this.storeid = storeid;
    }
    
}
