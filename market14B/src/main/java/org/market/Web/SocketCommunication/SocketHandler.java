package org.market.Web.SocketCommunication;

import org.market.DomainLayer.backend.NotificationPackage.DelayedNotifierDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketHandler extends TextWebSocketHandler {

	private final ConcurrentHashMap<String, List<WebSocketSession>> sessions;
	private final ApplicationContext applicationContext;

	@Autowired
	public SocketHandler(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.sessions = new ConcurrentHashMap<>();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String query = session.getUri().getQuery();
		String username = null;
		if (query != null && query.startsWith("username=")) {
			username = query.substring("username=".length());
		}

		if (username != null) {
			sessions.computeIfAbsent(username, k -> new ArrayList<>()).add(session);
			List<String> messages = applicationContext.getBean(DelayedNotifierDecorator.class).retrieveNotifications(username);
			if (messages != null) {
				for (String notification : messages) {
					session.sendMessage(new TextMessage(notification));
				}
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String query = session.getUri().getQuery();
		String username = null;
		if (query != null && query.startsWith("username=")) {
			username = query.substring("username=".length());
		}

		if (username != null) {
			List<WebSocketSession> userSessions = sessions.get(username);
			if (userSessions != null) {
				userSessions.remove(session);
				if (userSessions.isEmpty()) {
					sessions.remove(username);
				}
			}
		}
	}

	public void sendMessage(String username, String message) throws InterruptedException, IOException {
		List<WebSocketSession> userSessions = sessions.get(username);
		if (userSessions != null) {
			for (WebSocketSession session : userSessions) {
				if (session.isOpen()) {
					session.sendMessage(new TextMessage(message));
				}
			}
		}
	}

    public boolean hasUserSession(String username) {
		return sessions.get(username) != null;
    }
}