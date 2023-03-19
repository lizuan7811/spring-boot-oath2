package spring.boot.oath2.websecurity.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import spring.boot.oath2.websecurity.model.RoleDet;
import spring.boot.oath2.websecurity.repository.UserRepository;

@Configuration
public class UserRepositoryImpl<T> extends UserRepository<T>{
	
	@Autowired
	private EntityManager entityManager;

	public UserRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	}

	public T findByUsername(String username,Class<T> cla) {
		CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cQuery=criteriaBuilder.createQuery(cla);
		Root<T> root=cQuery.from(cla);
		Predicate predName =criteriaBuilder.equal(root.get("username"), username);
		
		cQuery.where(predName);
		TypedQuery<T> typeQuery=entityManager.createQuery(cQuery);
		typeQuery.getResultList().forEach(System.out::println);
		return typeQuery.getResultList().get(0);
	}

	public List<T> getRoleByuid(int id,Class<T> cla) {
		CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cQuery=criteriaBuilder.createQuery(cla);
		Root<T> root=cQuery.from(cla);
		Predicate predName =criteriaBuilder.equal(root.get("id"), id);
		cQuery.where(predName);
		TypedQuery<T> typeQuery=entityManager.createQuery(cQuery);
		return typeQuery.getResultList();
	}

}
