package Web.SocketCommunication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import DomainLayer.backend.NotificationPackage.DelayedNotifierDecorator;

@Component
public class SocketHandler extends TextWebSocketHandler {

	private final Map<String,WebSocketSession> sessions;
	private final ApplicationContext applicationContext;
	
	@Autowired
	public SocketHandler(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
		sessions = new ConcurrentHashMap<>();
	}


	@Override
	public void afterConnectionEstablished(WebSocketSession session)
            throws java.lang.Exception{
				String query = session.getUri().getQuery();
				String username = null;
				if (query != null && query.startsWith("username=")) {
					username = query.substring("username=".length());
				}
				sessions.put(username,session);
				List<String> nots = applicationContext.getBean(DelayedNotifierDecorator.class).retriveNotifications(username);
				if(nots != null){
					for (String notification : nots) {
						sessions.get(username).sendMessage(new TextMessage(notification));
					}
				}

	}


	@Override
	public void afterConnectionClosed(WebSocketSession session,CloseStatus status)     
			throws Exception{
				String query = session.getUri().getQuery();
				String username = null;
				if (query != null && query.startsWith("username=")) {
					username = query.substring("username=".length());
				}
				sessions.remove(username);
	}


	public void sendMessage(String username, String message)
			throws InterruptedException, IOException {
		if(sessions.get(username) != null){
			sessions.get(username).sendMessage(new TextMessage(message));
		}
    }
}
