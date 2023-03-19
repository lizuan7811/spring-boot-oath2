package spring.boot.oath2.websecurity;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	@Autowired
	private SelfUserDetailService selfUserDetailService;
//	@Autowired
//	public JavaWebSecurityConfiguration(SelfUserDetailService selfUserDetailService){
//		this.selfUserDetailService=selfUserDetailService;
//	}
	
	
	@Bean
	public LoginFilter loginFilter() {
		
		LoginFilter loginFilter=new LoginFilter();
		loginFilter.setFilterProcessesUrl("/doLogin");
		loginFilter.setUsernameParameter("uname");
		loginFilter.setPasswordParameter("passwd");
		loginFilter.setAuthenticationManager(authenticationManagerBean());
		
		loginFilter.setAuthenticationSuccessHandler((req,resp,authentication)->{
			Map<String,Object> result=new HashMap<String,Object>();
			result.put("msg","Login Successed");
			result.put("User Info: ", authentication.getPrincipal());
			resp.setContentType("application/json;charset=UTF-8");
			resp.setStatus(HttpStatus.OK.value());
			String s=new ObjectMapper().writeValueAsString(result);
			resp.getWriter().println(s);
		});
		
		loginFilter.setAuthenticationFailureHandler((req,resp,ex)->{
			Map<String,Object> result=new HashMap<String,Object>();
			result.put("msg","Fail Login: "+ ex.getMessage());
			resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resp.setContentType("application/json;charset=UTF-8");
			String s=new ObjectMapper().writeValueAsString(result);
			resp.getWriter().println(s);
		});
		return loginFilter;
	}
	
	
	
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		return selfUserDetailService;
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
	}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager userDetailsService=new InMemoryUserDetailsManager();
//		userDetailsService.createUser(User.withUsername("lizuan").password("{noop}Admin@@@111").build());
//		return userDetailsService;
//	}
	
//	有了UserDetailsService，預設的initilaize如果寫了相同的程式碼，就是多的
//	@Autowired
//	public void initialize(AuthenticationManagerBuilder builder) {
//		try {
//		InMemoryUserDetailsManager userDetailsService=new InMemoryUserDetailsManager();
//		userDetailsService.createUser(User.withUsername("lizuan").password("{noop}Admin@@@111").build());
//			builder.userDetailsService(userDetailsService);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	自定義的方法，全部都要自定義
	@Autowired
	public void configure(AuthenticationManagerBuilder builder) {
		try {
			System.out.println("自定義AuthenticationManager:\t"+builder);
			builder.userDetailsService(selfUserDetailService);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println("自定義AuthenticationManager:"+builder);
//		try {
//			builder.userDetailsService(userDetailsService());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
//	將AuthenticationManager開放讓其他地方是永
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() {
		AuthenticationManager authenticationManager=null;
		try {
			authenticationManager= super.authenticationManagerBean();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authenticationManager;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.mvcMatchers("/login.html").permitAll()
		.mvcMatchers("/index/*").permitAll().anyRequest()
				.authenticated().and().formLogin().loginPage("/login.html")
				.loginProcessingUrl("/doLogin")
				.usernameParameter("uname")
				.passwordParameter("passwd")
//		只適合傳統web開發
//		.successForwardUrl("/index/hello");

//		前後端分離方式
				.successHandler(new SelfDefiAuth())
				.failureHandler(new SelfDefiAuthFail())
				.and()
				.logout()
//				.logoutUrl("/logout")
				.logoutRequestMatcher(new OrRequestMatcher(
						new AntPathRequestMatcher("/aa","GET"),
						new AntPathRequestMatcher("/bb","POST")
						))
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutSuccessHandler(new SelfLogoutSuccessed())
//				.logoutSuccessUrl("/login.html")
//				.failureForwardUrl("/login.html")
//				.failureUrl("/login.html")
				.and().csrf().disable();
	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	public DaoAuthenticationProvider authProvider() {
//		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	}
}