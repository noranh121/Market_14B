package org.market.DomainLayer.backend.NotificationPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static org.market.DomainLayer.backend.UserPackage.UserController.notfications;

@Component
public class DelayedNotifierDecorator{
    public static final Logger LOGGER = Logger.getLogger(ImmediateNotifierDecorator.class.getName());

    private Map<String, List<String>> delayedMessages = new ConcurrentHashMap<>();

    private BaseNotifier wrappedNotifier;

    @Autowired
    public DelayedNotifierDecorator(BaseNotifier notifier) {
        this.wrappedNotifier = notifier;
    }

    public void send(String user, String message) {
        if (wrappedNotifier.isUserOnline(user)) {
            wrappedNotifier.send(user, message);
        } else {
            LOGGER.info("User " + user + " is offline. Saving message for later.");
            if(!delayedMessages.containsKey(user)){
                delayedMessages.put(user, Collections.synchronizedList(new ArrayList<>()));
            }
            delayedMessages.get(user).add(message);
        }
        notfications.add(new String[] {user, message});
    }

    // public void sendDelayedMessages(String user) {
    //     LOGGER.info("Sending delayed messages to " + user);
    //     for (String message : delayedMessages.get(user)) {
    //         super.send(user, message);
    //     }
    //     delayedMessages.remove(user);
    // }

    public List<String> retrieveNotifications(String username){
        if(delayedMessages.containsKey(username)){
            List<String> messages = delayedMessages.get(username);
            delayedMessages.remove(username);
            return messages;
        }
        return null;
    }



}
