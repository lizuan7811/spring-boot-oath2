package spring.boot.oath2.websecurity.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import spring.boot.oath2.websecurity.entity.Role;
import spring.boot.oath2.websecurity.entity.User;
import spring.boot.oath2.websecurity.model.UserDet;
import spring.boot.oath2.websecurity.repository.UserRepository;
import spring.boot.oath2.websecurity.repository.impl.UserRepositoryImpl;
@Component
public class SelfUserDetailService implements UserDetailsService{
	
	private final UserRepository<User> userRepository;
	private final UserRepository<Role> roleRepository;
	private final ConvertEntityToModel convertEntityToModel;
	
	@Autowired
	public SelfUserDetailService(UserRepository<User> userRepository,UserRepository<Role> roleRepository,ConvertEntityToModel convertEntityToModel) {
		this.userRepository=userRepository;
		this.roleRepository=roleRepository;
		this.convertEntityToModel=convertEntityToModel;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =userRepository.findByUsername(username,User.class);
		System.out.println(user);
		UserDet userDet= convertEntityToModel.convertEntity(user, new UserDet()) ;
		System.out.println(userDet);

		if(ObjectUtils.isEmpty(userDet))throw new UsernameNotFoundException("用戶不正確");
		List<Role>roles=roleRepository.getRoleByuid(userDet.getId(),Role.class);
		userDet.setRoles(roles);
		return userDet;
	}
}
