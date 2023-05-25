package spring.boot.oath2.websecurity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SelfDefiAuth implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.out.println(">>> onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,\r\n"
				+ "			Authentication authentication)");
		Map<String,Object>result=new HashMap<>();
		result.put("msg","LoginSuccessed");
		result.put("status",200);
		result.put("authentication",authentication);
		String s=new ObjectMapper().writeValueAsString(result);
		response.getWriter().print(s);
	}

}
