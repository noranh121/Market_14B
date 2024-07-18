package org.market.DomainLayer.backend.UserPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class User {

    private String username;
    private double age;
    private ShoppingCart shoppingCart;
    private boolean LoggedIn;
    private List<Integer> transaction_id_List;

    public User(String username,double age) {
        this.username = username;
        this.age=age;
        shoppingCart = new ShoppingCart();
        LoggedIn = false;
        transaction_id_List=Collections.synchronizedList(new ArrayList<>());
    }
    
    public double getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }



    public void setLoggedIn(boolean loggedIn) {
        LoggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return LoggedIn;
    }

    /**
     * checks shopping cart, availability, discounts.... (all or none)
     * 
     * @return total sum
     * @throws Exception
     */
    public double Buy() throws Exception {
        return shoppingCart.Buy(getUsername());
    }

    public String addToCart(Integer product, int storeId, int quantity) throws Exception {
        shoppingCart.addToCart(this.username, product, storeId, quantity);
        UserController.LOGGER.info("added to cart");
        return "added to cart";
    }

    public String addToCartOffer(Integer product, int storeId, double price) throws Exception {
        shoppingCart.addToCartOffer(this.username, product, price, storeId);
        UserController.LOGGER.info("added to cart");
        return "added to cart";
    }

    /**
     * extract cart baskets and items ....
     * 
     * @return String of cart contents
     */
    public String inspectCart() {
        return shoppingCart.inspectCart(this.username);
    }

    /**
     * remove item from cart
     * 
     * @param storeId
     * @param product
     * @return whether the removal was successful
     */
    public String removeCartItem(int storeId, int product) {
        return shoppingCart.removeCartItem(storeId, product);
    }

    protected Boolean reviewOffer(double offer) throws Exception{
        wait(1000);
        return true;
    }

    public Boolean addTransaction_id(Integer transaction_id) {
        if(!transaction_id_List.contains(transaction_id)){
            transaction_id_List.add(transaction_id);
            return true;
        }
        return false;
    }

    public void cleanShoppingCart() {
        shoppingCart.clean();
    }

    public double offerPrice(String username, int storeId, double price, int productId) {
        return price-10;
    }
}
