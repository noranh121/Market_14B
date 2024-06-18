package DomainLayer.backend.NotificationPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DelayedNotifierDecorator extends NotifierDecorator {

    private Map<String, List<String>> delayedMessages = new ConcurrentHashMap<>();

    public DelayedNotifierDecorator(Notifier notifier) {
        super(notifier);
    }

    @Override
    public void send(String user, String message) {
        if (isUserOnline(user)) {
            super.send(user, message);
        } else {
            LOGGER.info("User " + user + " is offline. Saving message for later.");
            if(!delayedMessages.containsKey(user)){
                delayedMessages.put(user, Collections.synchronizedList(new ArrayList<>()));
            }
            delayedMessages.get(user).add(message);
        }
    }

    public void sendDelayedMessages(String user) {
        LOGGER.info("Sending delayed messages to " + user);
        for (String message : delayedMessages.get(user)) {
            super.send(user, message);
        }
        delayedMessages.remove(user);
    }
}
