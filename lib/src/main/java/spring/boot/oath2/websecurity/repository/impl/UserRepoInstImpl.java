package spring.boot.oath2.websecurity.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import spring.boot.oath2.websecurity.entity.User;

@Component
public class UserRepoInstImpl extends UserRepositoryImpl<User>{

	public UserRepoInstImpl(Class<User> domainClass, EntityManager em) {
		super(domainClass, em);
	}

}
