package org.market.PresentationLayer.notifications;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;

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

        if (ui != null && ui.isAttached()) {
            ui.access(() -> Notification.show("WebSocket connection opened"));
        }
    }

    public void handleMessage(String message) {
        if (ui != null && ui.isAttached()) {
            ui.access(() -> Notification.show("Received message: " + message));
        }
    }

    public void handleClose() {
        if (ui != null && ui.isAttached()) {
            ui.access(() -> Notification.show("WebSocket connection closed"));
        }
    }

    public void handleError(Exception ex) {
        if (ui != null && ui.isAttached()) {
            ui.access(() -> Notification.show("WebSocket error: " + ex.getMessage()));
        }
    }
}
