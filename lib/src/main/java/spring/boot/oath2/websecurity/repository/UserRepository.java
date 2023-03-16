package spring.boot.oath2.websecurity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.boot.oath2.websecurity.model.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Integer> {
	List<UserInfo> findAll();
}
