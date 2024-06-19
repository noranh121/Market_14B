package DomainLayer.backend.NotificationPackage;

public class BaseNotifier implements Notifier {

    @Override
    public void send(String user, String message) {
        LOGGER.info("Sending message to " + user + "- " + message);
    }

}
