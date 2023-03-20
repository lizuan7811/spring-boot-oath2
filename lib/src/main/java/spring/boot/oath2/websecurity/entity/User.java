package spring.boot.oath2.websecurity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Data;

@Entity
@Data
@Table(name="usertb")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	@Column(name="enabled")
	private Boolean enabled;
	@Column(name="accountnonexpired")
	private Boolean accountNonExpired;
	@Column(name="accountnonlocked")
	private Boolean accountNonLocked;
	@Column(name="credentialsnonexpired")
	private Boolean credentialsNonExpired;
	
}
