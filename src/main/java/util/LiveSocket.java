/**
 * This class implements the Web Socket. It is not used yet.
 */

package util;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class LiveSocket {
	private Session session;

	public LiveSocket() {
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.printf("Got connect: %s%n", session);
		this.session = session;
	}

	public void SendMessage(String message) {
		if (session != null) {
			try {
				session.getRemote().sendString(message);
			} catch (Exception ex) {
				ex.printStackTrace();
				Logger.errorLogger(ex);
			}
		}
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		// we can ignore for now
	}
}