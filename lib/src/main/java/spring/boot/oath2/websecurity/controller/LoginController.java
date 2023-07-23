package spring.boot.oath2.websecurity.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

	@RequestMapping("/doLogin")
	public String doLogin() {
		System.out.println("doLogin");
		return "login";
	}

	@RequestMapping("/loginpage")
	public String loginpage() {
		
		System.out.println("loginpage");
		return "login";
	}
	
	@RequestMapping("/errorpage")
	public String loginError() {
		
		System.out.println("error");
		return "error";
	}
	
}
