/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
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
