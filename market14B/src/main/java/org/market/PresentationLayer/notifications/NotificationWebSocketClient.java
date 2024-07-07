package org.market.PresentationLayer.notifications;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class NotificationWebSocketClient extends WebSocketClient {

    private final NotificationHandler notificationHandler;

    public NotificationWebSocketClient(String serverUri, NotificationHandler notificationHandler) throws URISyntaxException {
        super(new URI(serverUri));
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        notificationHandler.handleOpen();
    }

    @Override
    public void onMessage(String message) {
        notificationHandler.handleMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        notificationHandler.handleClose();
    }

    @Override
    public void onError(Exception ex) {
        System.out.println(ex.getMessage());
        notificationHandler.handleError(ex);
    }
}