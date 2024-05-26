package DomainLayer.backend.NotificationPackage;

import DomainLayer.backend.UserPackage.User;
import DomainLayer.backend.UserPackage.UserController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class Notifier {
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    private HashMap<String, ArrayList<String>> delayeduserNotifications;

    public Notifier(){
        delayeduserNotifications = new HashMap<String, ArrayList<String>>();
    }

    public void notify(String username, String message) {
        User user = UserController.getInstance().getUser(username);
        if (user.isLoggedIn()){
            displaynotifications(username);
            ImmediateNotifier(username,message);
        }
        else {
            if (delayeduserNotifications.containsKey(username)){
                delayeduserNotifications.get(username).add(message);
            }
            else {
                ArrayList<String> notifications = new ArrayList<String>();
                notifications.add(message);
                delayeduserNotifications.put(username, notifications);
            }
        }
    }


    public void ImmediateNotifier(String username, String message){
        LOGGER.info("the user" + username + "got this notification" + message + "at" + LocalDateTime.now());
    }

    public ArrayList<String> displaynotifications(String username){
        ArrayList<String> notifications = delayeduserNotifications.get(username);
        for (int i = 0; i < notifications.size(); i++) {
            LOGGER.info("the user" + username + "got this notification" + notifications.get(i) + "at" + LocalDateTime.now());
        }
        delayeduserNotifications.remove(username);
        return notifications;
    }

}
