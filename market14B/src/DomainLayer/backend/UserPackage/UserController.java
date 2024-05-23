package DomainLayer.backend.UserPackage;

import java.util.HashMap;
import java.util.logging.Logger;

public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private static UserController instance;
    public static UserController getInstance() {
        return instance;
    }

    private HashMap<String,User> GuestMap = new HashMap<>();
    private int idCounter = 0;
    private HashMap<String,User> RegUserMap = new HashMap<>();

    // Guest
    public User EnterAsGuest() {
        User guest = new GuestUser(idCounter);
        idCounter++;
        addToGuestMap(guest);
        return guest;
    }

    private String addToGuestMap(User guest) {
        if (GuestMap.put(guest.getUsername(), guest) == null) {
            LOGGER.warning("guest user cannot be added");
            return "guest user cannot be added";
        }
        else {
            guest.setLoggedIn(true);
            LOGGER.info("guest user added successfully");
            return "guest user added successfully";
        }
    }

    public String GuestExit(String username) {
        if (GuestMap.remove(username) == null ) {
            LOGGER.warning("guest user cannot be deleted");
            return "guest user cannot be deleted";
        }
        LOGGER.info("guest existed successfully");
        return "guest existed successfully";
    }

    public String Login(String guest, String username, String password) {
        User user = RegUserMap.get(username);
        if (user == null || !authenticate(username,password)) {
            LOGGER.warning("username or password are incorrect");
            return "username or password are incorrect";
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
    private String addToRegUserMap(User reg) {
        if (RegUserMap.put(reg.getUsername(), reg) == null) {
            LOGGER.warning("guest user cannot be added");
            return "guest user cannot be added";
        }
        else return "guest user added successfully";
    }

    public User Logout(String username) {
        // save data - DATA SERVICE
        RegUserMap.get(username).setLoggedIn(false);
        return EnterAsGuest();
    }

    // both
    public String Register(String username, String password) {
        // encryption TODO
        User reg = new RegisteredUser(username,password);
        return addToRegUserMap(reg);
    }


}
