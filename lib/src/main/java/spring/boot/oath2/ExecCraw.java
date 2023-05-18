package spring.boot.oath2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "spring.boot.oath2.scrabdatas")
public class ExecCraw {
	public static void main(String[] args) {
//		設定代理
		System.setProperty("http.proxyHost", " 181.191.94.126");
		System.setProperty("http.proxyPort", "8999");
		System.setProperty("https.proxyHost", "181.191.94.126");
		System.setProperty("https.proxyPort", "8999");
		SpringApplication.run(ExecCraw.class, args);
	}

}
