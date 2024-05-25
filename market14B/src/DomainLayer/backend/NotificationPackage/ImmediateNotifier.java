package DomainLayer.backend.NotificationPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImmediateNotifier extends Notifier{
//    private HashMap<String, ArrayList<Notification>> userNotifications;
//
//    public ImmediateNotifier(){
//        userNotifications = new HashMap<String, ArrayList<Notification>>();
//    }
    @Override
    public void notify(String username, Notification notification) {
        System.out.println("the user" + username + "got this notification" + notification.getMessage());
    }
    // in user class each marketowner gets notified owner.notify(username , Notification)
}


//if (userNotifications.containsKey(username)) {
//        userNotifications.get(username).add(notification);
//        }
//        else {
//        ArrayList<Notification> notifications = new ArrayList<Notification>();
//        notifications.add(notification);
//        userNotifications.put(username, notifications);
//        }