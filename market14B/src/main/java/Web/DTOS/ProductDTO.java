package Web.DTOS;

import com.fasterxml.jackson.databind.util.ArrayBuilders.DoubleBuilder;

import DomainLayer.backend.ProductPackage.Product;

public class ProductDTO {

    private int id;
    private String name;
    private Double price;
    private String description;

    public ProductDTO(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = new Double(-1);
        this.description = product.getDescription();

    }
    
}
