package DomainLayer.backend.NotificationPackage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Notifier {

    public abstract void notify(String username, Notification notification);
}
