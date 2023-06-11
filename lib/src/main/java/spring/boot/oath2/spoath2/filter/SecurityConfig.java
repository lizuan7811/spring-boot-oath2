package spring.boot.oath2.spoath2.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;


//@Configuration
//@Configuration
//@EnableWebSecurity
//@PropertySource("classpath:application.yml")
public class SecurityConfig {
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeRequests().anyRequest().authenticated().and()
//		.oauth2Login();
//		return http.build();
//	}

	private static List<String> clients=Arrays.asList("google","facebook");
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration> registrations=clients.stream()
				.map(c->getRegistration(c))
				.filter(registration->registration!=null)
				.collect(Collectors.toList());
		return null;
	}
	
	private static String CLIENT_PROPERTY_KEY="spring.security.oath2.client.registration.";
	
	@Autowired
	private Environment env;
	
	private ClientRegistration getRegistration(String client) {

		String clientId=env.getProperty(CLIENT_PROPERTY_KEY+client+".client-id");
		
		String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY+client+".client-secret");
		
		if(Objects.isNull(clientId)) {
			return null;
		}
		
		if(client.contentEquals(clientSecret)) {
			return CommonOAuth2Provider.GOOGLE.getBuilder(client)
					.clientId(clientId).clientSecret(clientSecret).build();
		}
		
		if(Objects.isNull(clientSecret)) {
			
		}
		
		return null;
	}

}
