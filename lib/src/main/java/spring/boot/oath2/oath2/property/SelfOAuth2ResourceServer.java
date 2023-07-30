//package spring.boot.oath2.oath2.property;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.stereotype.Component;
//
//@Component
//@EnableResourceServer
//public class SelfOAuth2ResourceServer extends ResourceServerConfigurerAdapter{
//
//	private final TokenStore tokenStore;
//	@Autowired
//	public SelfOAuth2ResourceServer(TokenStore tokenStore) {
//		this.tokenStore=tokenStore;
//	}
//	
//	@Override
//	public void configure(HttpSecurity http)throws Exception{
//		http.authorizeRequests().antMatchers("/api/**")
//		.authenticated().antMatchers("/").permitAll();
//	}
//	
//	@Override
//	public void configure(ResourceServerSecurityConfigurer resources)throws Exception{
//		resources.tokenStore(tokenStore);
//	}
//	
//}
