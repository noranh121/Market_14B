package DomainLayer.backend.NotificationPackage;

import java.util.logging.Logger;

public class BaseNotifier implements Notifier {
    private static final Logger LOGGER = Logger.getLogger(Notifier.class.getName());

    @Override
    public void send(String user, String message) {
        LOGGER.info("Sending message to " + user + "- " + message);
    }

}
