/**
 * This class helps with the web sockets. It is not used yet.
 */


package websocket;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import util.Logger;


@WebSocket
public class TupleUploadSocketHandler {

	Thread groupCreateThread;
	
		
	static List<Session> sessions = new ArrayList<>();

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		sessions.add(session);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		sessions.remove(session);
	}

	@OnWebSocketMessage
	public void onMessage(Session cSession, String message) {
		sessions.forEach(session -> {
			if(session != cSession) {
			send(session, message);
			}
		});
	}
		
	public static void Send(String tuple) {
		sessions.forEach(session -> {
			send(session, tuple);
		});
	}

	private static void send(Session session, String text) {
		if (session.isOpen()) {
			try {
				session.getRemote().sendString(text);
			} catch (Exception ex) {
				ex.printStackTrace();
				Logger.errorLogger(ex);
			}
		}
	}
}
