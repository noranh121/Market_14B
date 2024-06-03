package DomainLayer.backend.UserPackage;

import DomainLayer.backend.ProductPackage.Product;

import java.util.logging.Logger;

public abstract class User {

    private String username;
    private ShoppingCart shoppingCart;
    private boolean LoggedIn;


    public User(String username) {
        this.username = username;
        shoppingCart = new ShoppingCart();
        LoggedIn = false;
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
     * @return total sum
     * @throws Exception
     */
    public double Buy() throws Exception {
        return shoppingCart.Buy();
    }

    public String addToCart(Integer product, int storeId, int quantity) throws Exception {
        shoppingCart.addToCart(this.username,product,storeId,quantity);
        UserController.LOGGER.info("added to cart");
        return "added to cart";
    }

    /**
     * extract cart baskets and items ....
     * @return String of cart contents
     */
    public String inspectCart() {
        return shoppingCart.inspectCart();
    }


    /**
     * remove item from cart
     * @param storeId
     * @param product
     * @return whether the removal was successful
     */
    public String removeCartItem(int storeId, int product) {
        return shoppingCart.removeCartItem(storeId,product);
    }

}
