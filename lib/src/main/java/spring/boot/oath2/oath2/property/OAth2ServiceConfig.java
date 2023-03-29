package spring.boot.oath2.oath2.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
public class OAth2ServiceConfig {
	
	private static final String DEMO_RESOURCE_ID="order";

	@Configuration
	@EnableAuthorizationServer 
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{
		@Autowired
		private AuthenticationManager authenticationManager;
		@Autowired
		private RedisConnectionFactory redisConnectionFacotry;
		
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) {
			try {
				clients.inMemory().withClient("client_1")
				.resourceIds(DEMO_RESOURCE_ID).authorizedGrantTypes("client_credentials","refresh_token")
				.scopes("select").authorities("client").secret("123456").and().withClient("client_2")
				.resourceIds(DEMO_RESOURCE_ID).authorizedGrantTypes("password","refresh_token").scopes("select")
				.authorities("client").secret("123456");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints)throws Exception{
			endpoints.tokenStore(new RedisTokenStore(redisConnectionFacotry)).authenticationManager(authenticationManager)
			.allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);
			
		}
		
		
		
		
	}
	
}
