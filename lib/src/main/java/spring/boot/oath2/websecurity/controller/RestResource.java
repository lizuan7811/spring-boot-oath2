package spring.boot.oath2.websecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.boot.oath2.oath2.model.UserProfile;

@Controller
public class RestResource {

	@RequestMapping("/api/user/me")
	public ResponseEntity<UserProfile> profile(){
		User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email=user.getUsername()+"@howtodoingjava.com";
		UserProfile profile=new UserProfile();
		profile.setName(user.getUsername());
		profile.setEmail(email);
		return ResponseEntity.ok(profile);
	}
	
}
