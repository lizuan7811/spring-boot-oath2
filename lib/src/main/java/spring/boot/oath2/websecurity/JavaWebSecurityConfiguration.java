package spring.boot.oath2.websecurity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.boot.oath2.websecurity.controller.SelfDefiAuth;
import spring.boot.oath2.websecurity.controller.SelfDefiAuthFail;
import spring.boot.oath2.websecurity.controller.SelfLogoutSuccessed;
import spring.boot.oath2.websecurity.filter.LoginFilter;
import spring.boot.oath2.websecurity.service.SelfUserDetailService;

@Configuration
@EnableWebSecurity
public class JavaWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final SelfUserDetailService selfUserDetailService;

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public JavaWebSecurityConfiguration(SelfUserDetailService selfUserDetailService, PasswordEncoder passwordEncoder) {
		this.selfUserDetailService = selfUserDetailService;
		this.passwordEncoder = passwordEncoder;
	}

//	將AuthenticationManager開放讓其他地方使用
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// .mvcMatchers("/loginpage")中的允許的URL是作為API去被CALL使用，而.loginPage("/loginpage")就代表當請求送進去後，將會打到/loginpage上，同時需要controller存在Mapping去接收，return
		// 的view也要存在templates資料夾中，這樣才能順利導到網頁。
		http.authorizeRequests().mvcMatchers("/loginpage").permitAll().anyRequest().authenticated().and().formLogin()
				.loginPage("/loginpage").loginProcessingUrl("/doLogin")
				// 預設驗證成功的URL
				.defaultSuccessUrl("/login")
				// 預設驗證失敗的URL
				.failureUrl("/login").usernameParameter("uname").passwordParameter("passwd")
				// 前後端分離方式
				// 驗證成功管理
				.successHandler(new SelfDefiAuth())
				// 驗證失敗處理
				.failureHandler(new SelfDefiAuthFail()).failureForwardUrl("/login.html")
				// 失敗URL
				.failureUrl("/errorpage").and()
				// 登出URL
				.logout()
				// 指定了logout時，請求接收使用的url
				.logoutUrl("/logout")
				.logoutRequestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/aa", "GET"),
						new AntPathRequestMatcher("/bb", "POST")))
				.invalidateHttpSession(true).clearAuthentication(true).logoutSuccessHandler(new SelfLogoutSuccessed())
				.logoutSuccessUrl("/logout").and().csrf().disable().sessionManagement().maximumSessions(1)
				.expiredUrl("/loginpage").expiredSessionStrategy(event -> {
					HttpServletResponse response = event.getResponse();
					Map<String, Object> result = new HashMap<>();
					result.put("status", 500);
					result.put("msg", "當前Session已失效");
					String s = new ObjectMapper().writeValueAsString(result);
					response.getWriter().println(s);
					response.flushBuffer();
				});
//		TODO 需要addFilter()
	}

//

	/**
	 * 自定義登入時需過慮使用的Filter。
	 */
	@Bean
	public LoginFilter loginFilter() throws Exception {

		LoginFilter loginFilter = new LoginFilter();
		loginFilter.setFilterProcessesUrl("/doLogin");
		loginFilter.setUsernameParameter("uname");
		loginFilter.setPasswordParameter("passwd");
		loginFilter.setAuthenticationManager(authenticationManagerBean());

		loginFilter.setAuthenticationSuccessHandler((req, resp, authentication) -> {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("msg", "Login Successed");
			result.put("User Info: ", authentication.getPrincipal());
			resp.setContentType("application/json;charset=UTF-8");
			resp.setStatus(HttpStatus.OK.value());
			String s = new ObjectMapper().writeValueAsString(result);
			resp.getWriter().println(s);
		});

		loginFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("msg", "Fail Login: " + ex.getMessage());
			resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.setContentType("application/json;charset=UTF-8");
			String s = new ObjectMapper().writeValueAsString(result);
			resp.getWriter().println(s);
		});
		return loginFilter;
	}

	/**
	 * 自定義使用者驗證資料從DB查詢。(有了UserDetailsService，預設的initilaize如果寫了相同的程式碼，就是多的)
	 */
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		return selfUserDetailService;
	}
////		持久化儲存的資料
//		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//		jdbcUserDetailsManager.setDataSource(dataSource);
////		jdbcUserDetailsManager.createUser(User.withUsername("admin").password("Admin@@@111").authorities("ADMIN").build());
////		不持久化儲存的資料
////		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
////		jdbcUserDetailsManager.setDataSource(dataSource);
////		manager.createUser(User.withUsername("admin").password("Admin@@@111").authorities("ADMIN").build());
////		manager.createUser(User.withUsername("user_1").password("User@@@111").authorities("USER").build());
//		return jdbcUserDetailsManager;

	/**
	 * 若使用自定義的身分驗證方法，所以使用者資料的來源就需要自定義 <br>
	 * 後為官方說明 ** {@link SecurityBuilder} used to create an
	 * {@link AuthenticationManager}. Allows for easily building in memory
	 * authentication, LDAP authentication, JDBC based authentication, adding
	 * {@link UserDetailsService}, and adding {@link AuthenticationProvider}'s.
	 *
	 */

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(selfUserDetailService).passwordEncoder(passwordEncoder)
				.userDetailsPasswordManager(selfUserDetailService);
	}

//	@Bean
//	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//		AuthenticationManagerBuilder authenticationManagerBuilder = http
//				.getSharedObject(AuthenticationManagerBuilder.class);
//		authenticationManagerBuilder.authenticationProvider(null).userDetailsService(selfUserDetailService)
//				.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
//				.userDetailsPasswordManager(selfUserDetailService);
//		return authenticationManagerBuilder.build();
//	}

//	@Bean
//	/**
//	 * 若已經有AuthenticationManagerBuilder身分驗證的方法，就不需要再另外配authProvider，因為AuthenticationManagerBuilder裡面已經存在驗證的方法。
//	 */
//	public DaoAuthenticationProvider authProvider() {
//		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	}

}