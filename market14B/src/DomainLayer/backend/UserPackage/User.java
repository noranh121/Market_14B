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
        shoppingCart = new ShoppingCart(getUsername());
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
        //TODO checkAvai.. in service
        //checks shopping cart, availability, discounts.... (all or none)
//        if (!shoppingCart.checkAvailability()) {
//            LOGGER.severe("some items are missing, cant perform checkout");
//            return "some items are missing, cant perform checkout";
//        }
        //TODO
        // shoppingCart.checkDiscounts(String username);
        // should purchase be performed here?
        shoppingCart = new ShoppingCart(getUsername());
        LOGGER.info("Your purchase was successful");
        return "Your purchase was successful";
    }

    public String AddToCart(Product product, int storeId, int quantity) throws Exception {
        shoppingCart.addToCart(product,storeId,quantity);
        LOGGER.info("added to cart");
        return "added to cart";
    }

    public String inspectCart() {
        //extract cart baskets and items ....
        return shoppingCart.inspectCart();
    }

    public String removeCartItem(int storeId, Product product) {
        //remove item from cart
        return shoppingCart.removeCartItem(storeId,product);
    }

}
