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
		
		int statusCode = httpURLConnection.getResponseCode();
		if (statusCode == HttpURLConnection.HTTP_MOVED_PERM || statusCode == HttpURLConnection.HTTP_MOVED_TEMP) {
			String location = httpURLConnection.getHeaderField("Location"); // 获取重定向后的新 URL 地址
			URL mvdUrl = new URL(location);
			httpURLConnection = (HttpURLConnection) mvdUrl.openConnection();
		}
		return httpURLConnection;
	}

	public static void disConnection() {
		httpURLConnection.disconnect();
	}
}
