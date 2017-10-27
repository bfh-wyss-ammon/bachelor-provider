import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.webSocket;

import websocket.TupleUploadSocketHandler;

public class MainInfo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		port(10011);
		webSocket("/live", TupleUploadSocketHandler.class);
		init();
	}

}
