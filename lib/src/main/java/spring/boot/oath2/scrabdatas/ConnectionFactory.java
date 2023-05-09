package spring.boot.oath2.scrabdatas;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionFactory {
	
	private static HttpURLConnection httpURLConnection;
	
	public static HttpURLConnection getConnectionInst(URL url) throws IOException {
		httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setRequestProperty("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
		httpURLConnection.setInstanceFollowRedirects(false); // 禁止自动重定向
//		System.out.println(">>> ResponseCode "+connection.getResponseCode());
		return httpURLConnection;
	}

	public static void disConnection() {
		httpURLConnection.disconnect();
	}
}
