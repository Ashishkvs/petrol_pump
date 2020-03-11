package com.imagegrafia.petrolpump.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.imagegrafia.petrolpump.entity.Role;
import com.imagegrafia.petrolpump.entity.UserAccount;
import com.imagegrafia.petrolpump.repository.UserRepository;

@Service
@Transactional
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserAccount userAccount = userRepository.findByEmail(email.toLowerCase());
		if (userAccount == null) {
			throw new UsernameNotFoundException("UserAccount doesn't Exists " + email);
		}
		//From entity to SpringSecurity type UserDetails implemented class User
		return buildSpringSecurityUser(userAccount);
	}
	private User buildSpringSecurityUser(UserAccount userAccount) {
		return new User(userAccount.getEmail(), userAccount.getPassword(),
				userAccount.isEnabled(), userAccount.isAccountNonExpired(), userAccount.isCredentialsNonExpired(), userAccount.isAccountNonLocked(), getAuthorities(userAccount.getRoles()));
	}

	private static List<GrantedAuthority> getAuthorities(List<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
	}

}
