package DomainLayer.backend.ProductPackage;

import java.util.HashMap;
import java.util.Map;

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

    private CategoryController() {
        categories = new HashMap<>();
        counterID = 0;
    }

    public Category getCategory(int catID) {
        if (categories.containsKey(catID)) {
            return categories.get(catID);
        }
        return null;
    }

    public void addCategory(String name) {
        Category cat = new Category(counterID, name);
        categories.put(counterID, cat);
        counterID++;
    }

    public void addCategory(String name, int parentCategoryID) {
        Category cat = new Category(counterID, name, parentCategoryID);
        categories.get(parentCategoryID).addSubCategory(counterID);
        categories.put(counterID, cat);
        counterID++;
    }

}
