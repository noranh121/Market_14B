package DomainLayer.backend.NotificationPackage;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import Web.SocketCommunication.SocketHandler;



@Component
public class ImmediateNotifierDecorator extends NotifierDecorator {


    @Autowired
    private  ApplicationContext applicationContext;
    

    @Autowired
    public ImmediateNotifierDecorator(Notifier notifier) {
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
        }
    }catch(Exception ex){}
    }
}
