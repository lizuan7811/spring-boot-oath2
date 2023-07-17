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
@Table(name="USERTB")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="USERNAME")
	private String username;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="ENABLED")
	private Boolean enabled;
	@Column(name="ACCOUNTNONEXPIRED")
	private Boolean accountNonExpired;
	@Column(name="ACCOUNTNONLOCKED")
	private Boolean accountNonLocked;
	@Column(name="CREDENTIALSNONEXPIRED")
	private Boolean credentialsNonExpired;
	
}
