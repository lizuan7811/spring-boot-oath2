package spring.boot.oath2.oath2.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.authentication.AuthenticationProviderBeanDefinitionParser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@Order(1)
public class SelfWebSecurityConfig extends WebSecurityConfigurerAdapter{
//	@Autowired
//	private JwtAccessTokenConverter accessTokenConverter;
//	@Autowired
//	private TokenStore tokenStore;
//	@Autowired
//	private ResourceServerTokenServices tokenServicess;


	@Override
	protected void configure(HttpSecurity http)throws Exception{
		http.antMatcher("/**").authorizeRequests().antMatchers("/oath/authorize**","/login**","/error**")
		.permitAll().and().authorizeRequests().anyRequest().authenticated()
		.and().formLogin().permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("Mars@@@7811")).roles("USER");
//		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//		return new DefaultJaasAuthenticationProvider();
//	}
}
