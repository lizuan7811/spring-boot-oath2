package spring.boot.oath2.websecurity.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import spring.boot.oath2.websecurity.entity.Role;


@Component
public class RoleRepoInstImpl extends UserRepositoryImpl<Role> {

	public RoleRepoInstImpl(Class<Role> domainClass, EntityManager em) {
		super(domainClass, em);
	}
}
