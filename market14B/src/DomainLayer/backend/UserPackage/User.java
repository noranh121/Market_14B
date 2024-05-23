package DomainLayer.backend.UserPackage;

import DomainLayer.backend.ProductPackage.Product;

import java.util.logging.Logger;

public abstract class User {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    private String username;
    private String password;
    private ShoppingCart shoppingCart;
    private boolean LoggedIn;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        shoppingCart = new ShoppingCart();
        LoggedIn = false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public String Buy() {
        //checks shopping cart, availability, discounts.... (all or none)
        return "";
    }

    public String AddToCart(Product product, int storeId, int quantity) {
        return "";
    }

    public String inspectCart() {
        //extract cart baskets and items ....
        return "";
    }

    public String removeCartItem(int storeId, Product product) {
        //remove item from cart
        return "";
    }

}
