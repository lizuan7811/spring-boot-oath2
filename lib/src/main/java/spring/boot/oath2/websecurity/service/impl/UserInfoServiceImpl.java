package spring.boot.oath2.websecurity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.boot.oath2.websecurity.model.UserInfo;
import spring.boot.oath2.websecurity.repository.UserRepository;
import spring.boot.oath2.websecurity.service.UserInfoService;
@Service
public class UserInfoServiceImpl implements UserInfoService{
	
	@Autowired
	private UserRepository userRepository;
	@Override
	public List<UserInfo> findAll() {
		return userRepository.findAll();
	}
	@Override
	public void printHello() {
		System.out.println("HELLO");		
	}
	
	
	
	
}
