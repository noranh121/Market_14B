package org.market.PresentationLayer.notifications;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.net.URISyntaxException;

public class NotificationHandler {

    private NotificationWebSocketClient webSocketClient;
    private UI ui;

    public NotificationHandler(UI ui) {
        this.ui = ui;
    }

    public void connect(String serverUri, String username) {
        try {
            webSocketClient = new NotificationWebSocketClient(serverUri + "?username=" + username, this);
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }

    public void handleOpen() {
        Notification notification = new Notification("WebSocket connection opened", 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        if (ui != null && ui.isAttached()) {
            ui.access(notification::open);
        }
    }

    public void handleMessage(String message) {
        Notification notification = new Notification("Received message: " + message, 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        if (ui != null && ui.isAttached()) {
            ui.access(notification::open);
        }
    }

    public void handleClose() {
        Notification notification = new Notification("WebSocket connection closed", 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        if (ui != null && ui.isAttached()) {
            ui.access(notification::open);
        }
    }

    public void handleError(Exception ex) {
        Notification notification = new Notification("WebSocket error: " + ex.getMessage(), 3000);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        if (ui != null && ui.isAttached()) {
            ui.access(notification::open);
        }
    }
}
