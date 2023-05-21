package spring.boot.oath2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import spring.boot.oath2.scrabdatas.property.ScrawProperty;

@SpringBootApplication(scanBasePackages = "spring.boot.oath2.scrabdatas")
@ConfigurationPropertiesScan
public class ExecCraw {
	@Autowired
	private static ScrawProperty scrawProperty;
	public static void main(String[] args) {
//		設定代理
//		System.setProperty("http.proxyHost", "65.108.250.129");
//		System.setProperty("http.proxyPort", "8080");
//		System.setProperty("https.proxyHost", "65.108.250.129");
//		System.setProperty("https.proxyPort", "8080");
		SpringApplication.run(ExecCraw.class, args);
	}
}
