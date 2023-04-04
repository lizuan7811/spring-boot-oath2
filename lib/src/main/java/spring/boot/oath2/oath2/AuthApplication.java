package spring.boot.oath2.oath2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;


@SpringBootApplication(scanBasePackages = {"spring.boot.oath2.oath2"})
@EnableJpaRepositories(basePackages = "spring.boot.oath2.oath2")
public class AuthApplication {
	
	private static final Logger LOG=LoggerFactory.getLogger(AuthApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
	
//	@Bean
//	ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener(ServerProperties serverProperties,KeycloakServerProperties keycloakServerProperties){
//		return (evt)->{
//			Integer port=serverProperties.getPort();
//			String keycloakContextPath=keycloakServerProperties.getContextPath();
//			LOG.info("Embedded Keycloak started: http://localhost:{}{} to use keycloak",port,keycloakContextPath);
//		};
//	}
	
}
