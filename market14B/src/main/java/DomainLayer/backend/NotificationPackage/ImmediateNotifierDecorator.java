package DomainLayer.backend.NotificationPackage;

class ImmediateNotifierDecorator extends NotifierDecorator {
    

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
