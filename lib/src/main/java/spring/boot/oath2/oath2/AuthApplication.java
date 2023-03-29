package spring.boot.oath2.oath2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"spring.boot.oath2.oath2"})
@EnableJpaRepositories(basePackages = "spring.boot.oath2.oath2")
public class AuthApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
