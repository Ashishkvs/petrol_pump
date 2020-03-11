package com.imagegrafia.petrolpump.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.imagegrafia.petrolpump.entity.Role;
import com.imagegrafia.petrolpump.entity.UserAccount;
import com.imagegrafia.petrolpump.repository.RoleRepository;
import com.imagegrafia.petrolpump.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAccountService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	public UserAccount createUser(UserAccount user) {
		user.setEmail(user.getEmail().toLowerCase());
		String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encoded);

		// SET Additional param for Authentication
		user.setEnabled(true);
		user.setAccountNonExpired(true);
		user.setCredentialsNonExpired(true);
		user.setAccountNonLocked(true);
		UserAccount newUser = userRepository.save(user);

		// SET DEFAULT ROLE FOR NEW USER
		Role role = new Role();
		role.setRole("ROLE_USER ");
		role.setUserAccount(newUser);
		roleRepository.save(role);

		return newUser;
	}
	public UserAccount findUserAccountByPrincipal(Principal principal) {
		// TODO Auto-generated method stub
		log.info("Principal UserName : {}",principal.getName());
//		UserAccountService userAccountService=new UserAccountService();
		UserAccount userAccount = userRepository.findByEmail(principal.getName());
		return userAccount;

	}

}
