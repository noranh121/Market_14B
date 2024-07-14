package org.market.DomainLayer.backend.ProductPackage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class CategoryController {
    private static CategoryController instance;

    private Map<Integer, Category> categories; // catID ==> category
    private int counterID;

    public static synchronized CategoryController getinstance() {
        if (instance == null) {
            instance = new CategoryController();
        }
        return instance;
    }

    //this is for testing
    public void clear(){
        categories.clear();
        counterID=0;
    }

    private CategoryController() {
        categories = new ConcurrentHashMap<>();
        counterID = 0;
    }

    public Category getCategory(int catID) {
        if (categories.containsKey(catID)) {
            return categories.get(catID);
        }
        return null;
    }

    public Category getCategorybyName(String catName) {
        for (Category category : categories.values()) {
            if (category.getName().equals(catName)) {
                return category;
            }
        }
        return null;  
    }

    public int addCategory(String name) {
        Category cat = new Category(counterID, name);
        categories.put(counterID, cat);
        counterID++;
        return counterID-1;
    }

    public void addCategory(String name, int parentCategoryID) {
        Category cat = new Category(counterID, name, parentCategoryID);
        categories.get(parentCategoryID).addSubCategory(counterID);
        categories.put(counterID, cat);
        counterID++;
    }

}
