package org.market.Web.Requests;

public class SearchEntity {
    private String searchBy; // keyword, productname, category
    private boolean inStore;
    private int storeID;
    private String productName;
    private String categoryName;
    private String keyword;

    // filters
    private boolean range;
    private double min;
    private double max;
    private boolean prodRating;
    private boolean storeRating;
    private boolean category;

    public SearchEntity(String searchBy, boolean inStore,int storeID ,String productName, String categoryName, String keyword,
                        boolean range, double min, double max, boolean prodRating, boolean storeRating, boolean category) {
        this.searchBy = searchBy;
        this.inStore = inStore;
        this.storeID = storeID;
        this.productName = productName;
        this.categoryName = categoryName;
        this.keyword = keyword;
        this.range = range;
        this.min = min;
        this.max = max;
        this.prodRating = prodRating;
        this.storeRating = storeRating;
        this.category = category;
    }

    public SearchEntity(){}

    // Getters and Setters
    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public boolean isInStore() {
        return inStore;
    }

    public void setInStore(boolean inStore) {
        this.inStore = inStore;
    }

    public int getStoreID(){
        return this.storeID;
    }

    public void setStoreID(int storeID){
        this.storeID = storeID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isRange() {
        return range;
    }

    public void setRange(boolean range) {
        this.range = range;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public boolean isProdRating() {
        return prodRating;
    }

    public void setProdRating(boolean prodRating) {
        this.prodRating = prodRating;
    }

    public boolean isStoreRating() {
        return storeRating;
    }

    public void setStoreRating(boolean storeRating) {
        this.storeRating = storeRating;
    }

    public boolean isCategory() {
        return category;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }
}
