package org.market.DomainLayer.backend.NotificationPackage;


import org.market.Web.SocketCommunication.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static org.market.DomainLayer.backend.UserPackage.UserController.notfications;

@Component
public class ImmediateNotifierDecorator extends NotifierDecorator {


    @Autowired
    private  ApplicationContext applicationContext;
    

    @Autowired
    public ImmediateNotifierDecorator(@Lazy Notifier notifier) {
        super(notifier);
    }

    @Override
    public void send(String user, String message) {
        try{
        //send to socketImmediately
        if (!isUserOnline(user)) {
            LOGGER.info("User " + user + " is offline. Ignoring message.");
        } else {
            super.send(user, message);
            SocketHandler socket =  applicationContext.getBean(SocketHandler.class);
            socket.sendMessage(user,message);
            notfications.add(new String[] {user, message});
        }
    }catch(Exception ex){}
    }
}
