package com.imagegrafia.petrolpump.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude= {"roles","pump"})
@SequenceGenerator(initialValue=1,name="seq",allocationSize=100)
public class UserAccount {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="seq")
	private int id;
	private String email;
	private String name;
	private String password;
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean accountNonLocked;
	
	@OneToMany(mappedBy="userAccount")
	private List<Role> roles;
	
	@OneToOne(mappedBy="userAccount")
	private Pump pump;
}
