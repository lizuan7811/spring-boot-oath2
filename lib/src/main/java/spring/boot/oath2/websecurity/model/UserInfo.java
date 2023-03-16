package spring.boot.oath2.websecurity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="USER_INFO")
//@AllArgsConstructor
//@NoArgsConstructor
public class UserInfo {
	
	@Id
	private Integer uId;
	@Column(name="username")
	private String userName;
	@Column(name="password")
	private String pswd;
	@Column(name="enabled")
	private int enable;

}
