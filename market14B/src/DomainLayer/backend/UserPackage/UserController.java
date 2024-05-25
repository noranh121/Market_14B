package DomainLayer.backend.UserPackage;

import DomainLayer.backend.ProductPackage.Product;

import java.util.HashMap;
import java.util.logging.Logger;

public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private static UserController instance;
    public static UserController getInstance() {
        if (instance == null)
            return new UserController();
        return instance;
    }

    private HashMap<String,User> GuestMap = new HashMap<>(); //?
    private int idCounter = 0;
    private HashMap<String,User> RegUserMap = new HashMap<>();

    // Guest
    public String EnterAsGuest() throws Exception {
        User guest = new GuestUser(idCounter);
        idCounter++; //TODO how to keep track of available ids
        return addToGuestMap(guest);
    }

    private String addToGuestMap(User guest) throws Exception {
        if (GuestMap.put(guest.getUsername(), guest) == null) {
            LOGGER.severe("guest user cannot be added");
            throw new Exception("guest user cannot be added");
        }
        else {
            guest.setLoggedIn(true);
            LOGGER.info("guest user added successfully");
            return "guest user added successfully";
        }
    }

    public String GuestExit(String username) throws Exception{
        if (GuestMap.remove(username) == null ) {
            LOGGER.severe("guest user cannot be deleted");
            throw new Exception ("guest user cannot be deleted");
        }
        LOGGER.info("guest existed successfully");
        return "guest existed successfully";
    }

    public String Login(String guest, String username, String password) throws Exception {
        User user = RegUserMap.get(username);
        if (user == null || !authenticate(username,password)) {
            LOGGER.severe("username or password are incorrect");
            throw new Exception("username or password are incorrect");
        }
        else {
            LOGGER.info("logged in successfully");
            GuestExit(guest);
            return "logged in successfully";
        }
    }

    private boolean authenticate(String username, String password) {
        // send to authenticator TODO
        return false;
    }

    //Registered user
    private String addToRegUserMap(User reg) throws Exception {
        if (RegUserMap.put(reg.getUsername(), reg) == null) {
            LOGGER.severe("guest user cannot be added");
            throw new Exception("guest user cannot be added");
        }
        else return "guest user added successfully";
    }

    public String Logout(String username) throws Exception {
        // save data - DATA SERVICE
        RegUserMap.get(username).setLoggedIn(false);
        return EnterAsGuest();
    }

    // both
    public String Register(String username, String password) throws Exception {
        // encryption TODO
        if (RegUserMap.containsKey(username)) {
            LOGGER.severe("username already exists");
            throw new Exception("username already exists");
        }
        User reg = new RegisteredUser(username,password);
        return addToRegUserMap(reg);
    }

    public double Buy(String username) throws Exception {
        User user = getUser(username);
        if (user != null)
            return user.Buy();
        LOGGER.severe("user not found");
        throw new Exception("user not found");
    }

    private User getUser(String username) {
        if (RegUserMap.containsKey(username))
            return RegUserMap.get(username);
        else if (GuestMap.containsKey(username))
            return GuestMap.get(username);
        LOGGER.severe("username not found");
        return null;
    }

    public String addToCart(String username, Product product, int storeId, int quantity) throws Exception {
        return getUser(username).addToCart(product, storeId, quantity);
    }
    public String inspectCart(String username) {
        return getUser(username).inspectCart();
    }

    public String removeCartItem(String username, int storeId, Product product) {
        return getUser(username).removeCartItem(storeId, product);
    }

}
