package spring.boot.oath2.websecurity.service;

import java.util.List;

import spring.boot.oath2.websecurity.model.UserInfo;


public interface UserInfoService {
	
	public List<UserInfo> findAll();
	
	public void printHello();

}
