package DomainLayer.backend.NotificationPackage;

import java.util.logging.Logger;

public interface Notifier {
    static final Logger LOGGER = Logger.getLogger(Notifier.class.getName());
    void send(String user, String message);
}

