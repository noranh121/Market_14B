package org.market.DomainLayer.backend.UserPackage;

//import org.market.DataAccessLayer.DataController;

import org.market.DomainLayer.backend.AuthenticatorPackage.Authenticator;
import org.market.DomainLayer.backend.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Component("BackendUserController")
public class UserController {
    public static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Lazy
    @Autowired
    private Permissions permissions;
    public static List<String[]> notfications=new ArrayList<String[]>();
//    private DataController dataController;


    private UserController() {
        try {
            FileHandler fileHandler = new FileHandler("UserPackage", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }

    private Map<String, User> GuestMap = new ConcurrentHashMap<>(); // ?
    private int idCounter = 0;
    private Map<String, User> RegUserMap = new ConcurrentHashMap<>();

    // Guest
    public String EnterAsGuest(double age) throws Exception {
        User guest = new GuestUser(idCounter,age);
        idCounter++;
        return addToGuestMap(guest);
    }

    private String addToGuestMap(User guest) throws Exception {
        GuestMap.put(guest.getUsername(), guest);
        guest.setLoggedIn(true);
        LOGGER.info("guest user added successfully");
        return guest.getUsername();
    }

    public String GuestExit(String username) throws Exception {
        LOGGER.info("username: " + username);
        if (GuestMap.remove(username) == null) {
            LOGGER.severe("guest user cannot be deleted");
            throw new Exception("guest user cannot be deleted");
        }

        LOGGER.info("guest existed successfully");
        return "guest existed successfully";
    }

    public String Login(String guest, String username, String password) throws Exception {
        LOGGER.info("guest: " + guest + ", username: " + username + ", password: " + password);
        User user = RegUserMap.get(username);
        if (user == null || !authenticate(username, password)) {
            LOGGER.severe("username or password are incorrect");
            throw new Exception("username or password are incorrect");
        }
        else if(user.isLoggedIn()){
            LOGGER.severe("Failed to login");
            throw new Exception("Failed to login");
        }
        else {
            LOGGER.info("logged in successfully");
            user.setLoggedIn(true);
            GuestExit(guest);
            return "logged in successfully";
        }
    }

    private boolean authenticate(String username, String password) throws Exception {
        LOGGER.info("username: " + username + ", password: " + password);
        if (isRegistered(username)) {
            String pass = ((RegisteredUser) (RegUserMap.get(username))).getPassword();
            LOGGER.info("authenticated");
            return Authenticator.matches(password, pass);
        }
        LOGGER.severe("");
        return false;
    }

    // Registered user
    private String addToRegUserMap(User reg,String newPass) throws Exception {
        RegUserMap.put(reg.getUsername(), reg);
        //dataController.Register(reg.getUsername(), newPass,reg.getAge());
        return "User registered successfully";
    }

    public String Logout(String username) throws Exception {
        // save data - DATA SERVICE
        RegUserMap.get(username).setLoggedIn(false);
        return EnterAsGuest(RegUserMap.get(username).getAge());
    }

    // both
    public String Register(String username, String password,double age) throws Exception {
        LOGGER.info("username: " + username + ", password: " + password);
        String newPass = Authenticator.encodePassword(password);
        if (RegUserMap.containsKey(username)) {
            LOGGER.severe("username already exists");
            throw new Exception("username already exists");
        }
        User reg = new RegisteredUser(username, newPass,age);
        return addToRegUserMap(reg,newPass);
    }

    public double Buy(String username) throws Exception {
        LOGGER.info("username: " + username);
        User user = getUser(username);
        if (user != null)
            return user.Buy();
        LOGGER.severe("user not found");
        throw new Exception("user not found");
    }

    public User getUser(String username) {
        LOGGER.info("username: " + username);
        if (RegUserMap.containsKey(username))
            return RegUserMap.get(username);
        else if (GuestMap.containsKey(username))
            return GuestMap.get(username);
        LOGGER.severe("username not found");
        return null;
    }

    public String addToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        LOGGER.info("username: " + username + ", product: " + product + ", quantity: " + quantity);
        return getUser(username).addToCart(product, storeId, quantity);
    }

    public String addToCartOffer(String username, Integer product, double price, int storeId) throws Exception {
        LOGGER.info("username: " + username + ", product: " + product + ", price: " + price);
        return getUser(username).addToCartOffer(product, storeId, price);
    }

    public String inspectCart(String username) {
        LOGGER.info("username: " + username);
        return getUser(username).inspectCart();
    }

    public String removeCartItem(String username, int storeId, int product) {
        LOGGER.info("username: " + username + ", product: " + product + ", storeId: " + storeId);
        return getUser(username).removeCartItem(storeId, product);
    }

    public String EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
                                  Boolean storeManager, Boolean[] pType) throws Exception {
        LOGGER.info("storeID: " + storeID + ", ownerUserName: " + ownerUserName + ", userName: " + userName
                + "storeOwner: " + storeOwner);
        if (RegUserMap.containsKey(ownerUserName)) {
            RegisteredUser owner = (RegisteredUser) (RegUserMap.get(ownerUserName));
            return permissions.editPermission(storeID, owner.getUsername(), userName, storeOwner, storeManager,
                    pType);
        } else {
            LOGGER.severe("ownerUserName not found");
            throw new Exception("ownerUserName not found");
        }
    }

    public String AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        LOGGER.info("ownerUserName: " + ownerUserName + ", username: " + username + "storeOwner: " + storeId);
        if (RegUserMap.containsKey(ownerUserName)) {
            RegisteredUser owner = (RegisteredUser) (RegUserMap.get(ownerUserName));
            return permissions.addPermission(storeId, owner.getUsername(), username, false, true, pType);
        } else {
            LOGGER.severe("ownerUserName not found");
            throw new Exception("ownerUserName not found");
        }
    }

    public String AssignStoreOwner(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        LOGGER.info("ownerUserName: " + ownerUserName + ", username: " + username + "storeOwner: " + storeId);
        if (RegUserMap.containsKey(ownerUserName)) {
            RegisteredUser owner = (RegisteredUser) (RegUserMap.get(ownerUserName));
            return permissions.addPermission(storeId, owner.getUsername(), username, true, false, pType);
        } else {
            LOGGER.severe("ownerUserName not found");
            throw new Exception("ownerUserName not found");
        }
    }

    public Boolean isRegistered(String username) throws Exception {
        LOGGER.info("username: " + username);
        if (RegUserMap.containsKey(username))
            return true;
        else {
            LOGGER.severe(username + " is not registered");
            throw new Exception(username + " is not registered");
        }
    }

    public Map<String, User> getRegUserMap() {
        return RegUserMap;
    }

    public Map<String, User> getGuestUserMap() {
        return GuestMap;
    }


    public Boolean reviewOffer(double offer, String username) throws Exception {
        LOGGER.info("offer: " + offer);
        if(RegUserMap.containsKey(username)){
            return RegUserMap.get(username).reviewOffer(offer);
        }
        else{
            throw new Exception("user is not registered");
        }
    }

}
