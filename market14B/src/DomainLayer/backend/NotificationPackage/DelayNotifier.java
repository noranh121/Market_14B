package DomainLayer.backend.NotificationPackage;

import java.util.ArrayList;
import java.util.HashMap;

public class DelayNotifier extends Notifier {

    private HashMap<String, ArrayList<Notification>> delayeduserNotifications;

    public DelayNotifier(){
        delayeduserNotifications = new HashMap<String, ArrayList<Notification>>();
    }
    @Override
    public void notify(String username, Notification notification) {
        if (delayeduserNotifications.containsKey(username)) {
            delayeduserNotifications.get(username).add(notification);
        }
        else {
            ArrayList<Notification> notifications = new ArrayList<Notification>();
            notifications.add(notification);
            delayeduserNotifications.put(username, notifications);
        }
    }

    public ArrayList<Notification> displaynotifications(String username){
        ArrayList<Notification> notifications = delayeduserNotifications.get(username);
        for ( int i = 0 ; i < notifications.size() ; i++){
            System.out.println("the user" + username + "got this notification" + notifications.get(i).getMessage());
        }
        delayeduserNotifications.remove(username);
        return notifications;
    }
    //this should be called by the user when the user is online

}
