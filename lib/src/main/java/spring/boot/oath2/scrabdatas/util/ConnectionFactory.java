package spring.boot.oath2.scrabdatas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;

public class ConnectionFactory {

	private static HttpsURLConnection httpURLConnection;
	private static BufferedReader bufReader=null;

	public static HttpsURLConnection getConnectionInst(URL url) throws IOException {
		httpURLConnection = (HttpsURLConnection) url.openConnection();
//		httpURLConnection.connect();
//		HttpsURLConnection httpsURLConnection =(javax.net.ssl.HttpsURLConnection) url.openConnection();
//		httpsURLConnection.connect();
		
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setRequestProperty("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
		httpURLConnection.setInstanceFollowRedirects(false); // 禁止自动重定向
		httpURLConnection.setRequestProperty("Connection","keep-alive");
		int statusCode = httpURLConnection.getResponseCode();
		if (statusCode == HttpURLConnection.HTTP_MOVED_PERM || statusCode == HttpURLConnection.HTTP_MOVED_TEMP) {
			String location = httpURLConnection.getHeaderField("Location"); // 获取重定向后的新 URL 地址
			URL mvdUrl = new URL(location);
			httpURLConnection = (HttpsURLConnection) mvdUrl.openConnection();
		}
		httpURLConnection.getServerCertificates();

		return httpURLConnection;
	}

	public static void disConnection() {
		try {
			bufReader.close();
			httpURLConnection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedReader getBufferReader(HttpURLConnection connection) {
		try {
			bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufReader;
	}
	
	public static long randMill() {
		return new SecureRandom().nextInt(3000);
	}
	
}
