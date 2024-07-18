package org.market.DomainLayer.backend.ProductPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.market.DomainLayer.backend.Market;

public class Category {
    private int id;
    private String name;
    private int parentCategoryID;
    private List<Integer> subCategories;
    private List<Integer> products;

    public Category(int id, String name, int parentCategoryID) {
        this.id = id;
        this.name = name;
        this.parentCategoryID = parentCategoryID;
        subCategories = Collections.synchronizedList(new ArrayList<>());
        products = Collections.synchronizedList(new ArrayList<>());
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        parentCategoryID = -1;
        subCategories = Collections.synchronizedList(new ArrayList<>());
        products = Collections.synchronizedList(new ArrayList<>());
    }


    public List<Integer> getAllProductIds() {
        List<Integer> allProductIds = new ArrayList<>();
        getAllProductIdsRec(this, allProductIds);
        return allProductIds;
    }

    private void getAllProductIdsRec(Category category, List<Integer> allProductIds) {
        // Add the product IDs of the current category
        if (category.getProducts() != null) {
            allProductIds.addAll(category.getProducts());
        }

        // Recursively add product IDs from subcategories
        if (category.getSubCategories() != null) {
            for (Integer subCategoryId : category.getSubCategories()) {
                Category subCategory = CategoryController.getinstance().getCategory(subCategoryId);
                if (subCategory != null) {
                    getAllProductIdsRec(subCategory, allProductIds);
                }
            }
        }
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

    public int getParentCategory() {
        return parentCategoryID;
    }

    public void setParentCategory(int parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    public List<Integer> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Integer> subCategories) {
        this.subCategories = subCategories;
    }

    public List<Integer> getProducts() {
        return products;
    }

    public void setProducts(List<Integer> products) {
        this.products = products;
    }

    public void addSubCategory(int subCategoryID) {
        subCategories.add(subCategoryID);
    }

    public void removeSubCategory(int subCategoryID) {
        subCategories.remove(subCategoryID);
    }

    public void addProduct(int productID) {
        products.add(productID);
        if (parentCategoryID != -1) {
            Market.getCC().getCategory(parentCategoryID).addProduct(productID);
        }
    }

    public void removeProduct(int productID) {
        products.remove(productID);
        for (Integer categoryID : subCategories) {
            Market.getCC().getCategory(categoryID).removeProduct(productID);
        }
    }

}
