package org.market.DomainLayer.backend.NotificationPackage;


public abstract class NotifierDecorator implements Notifier {
    
    protected Notifier wrappedNotifier;

    public NotifierDecorator(Notifier notifier) {
        this.wrappedNotifier = notifier;
    }

    @Override
    public void send(String user, String message) {
        wrappedNotifier.send(user, message);
    }

    protected boolean isUserOnline(String user) {
        // Checks if the user is online
        return false;
    }
}
