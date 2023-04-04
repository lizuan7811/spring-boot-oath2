package spring.boot.oath2.oath2.property;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
@EnableResourceServer
public class OAth2ResourceServer extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http)throws Exception{
//		http.authorizeRequests()
//		.antMatchers("/api/**").authenticated()
//		.antMatchers("/").permitAll();
		
		http.authorizeRequests().antMatchers("/api/**")
		.authenticated().antMatchers("/").permitAll();
	}
	
	
}
