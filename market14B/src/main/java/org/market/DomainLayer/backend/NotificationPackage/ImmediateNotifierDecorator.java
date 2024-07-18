package org.market.DomainLayer.backend.NotificationPackage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import static org.market.DomainLayer.backend.UserPackage.UserController.notfications;

@Component
public class ImmediateNotifierDecorator {

    public static final Logger LOGGER = Logger.getLogger(ImmediateNotifierDecorator.class.getName());

    private BaseNotifier wrappedNotifier;

    @Autowired
    public ImmediateNotifierDecorator(BaseNotifier notifier) {
        this.wrappedNotifier = notifier;
    }

    public void send(String user, String message) {
        try{
            notfications.add(new String[] {user, message});
            //send to socketImmediately
            if (!wrappedNotifier.isUserOnline(user)) {
                LOGGER.info("User " + user + " is offline. Ignoring message.");
            } else {
                wrappedNotifier.send(user, message);;
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

    }
}
