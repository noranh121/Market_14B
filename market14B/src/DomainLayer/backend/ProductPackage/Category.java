package DomainLayer.backend.ProductPackage;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private Category parentCategory;
    private List<Category> subCategories;
    private List<Product> products;


    public Category(int id, String name, Category parentCategory){
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
        subCategories = new ArrayList<>();
        products = new ArrayList<>();
    }

    public Category(int id, String name){
        this.id = id;
        this.name = name;
        parentCategory = null;
        subCategories = new ArrayList<>();
        products = new ArrayList<>();
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

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public void addSubCategory(Category subCategory) {
        subCategories.add(subCategory);
    }

    public void removeSubCategory(Category subCategory) {
        subCategories.remove(subCategory);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }


}

