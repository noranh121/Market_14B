package org.market.Web.Requests;

import java.time.LocalDate;

public class addPurchaseReq {
    int quantity;
    double price;
    LocalDate date;
    int atLeast;
    double weight;
    double age;
    //int id;
    String username;
    int storeId;
    String categoryName;
    String logicalRule;
    String productName;

    public addPurchaseReq(){}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAtLeast() {
        return atLeast;
    }

    public void setAtLeast(int atLeast) {
        this.atLeast = atLeast;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getLogicalRule() {
        return logicalRule;
    }

    public void setLogicalRule(String logicalRule) {
        this.logicalRule = logicalRule;
    }

}
