package DomainLayer.backend.NotificationPackage;

import java.util.logging.Logger;

class ImmediateNotifierDecorator extends NotifierDecorator {
    private static final Logger LOGGER = Logger.getLogger(Notifier.class.getName());

    public ImmediateNotifierDecorator(Notifier notifier) {
        super(notifier);
    }

    @Override
    public void send(String user, String message) {
        if (!isUserOnline(user)) {
            LOGGER.info("User " + user + " is offline. Ignoring message.");
        } else {
            super.send(user, message);
        }
    }
}
