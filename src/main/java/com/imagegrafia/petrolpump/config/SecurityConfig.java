package com.imagegrafia.petrolpump.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * This is spring security provided interface will be implemented by
	 * CustomUserDetailService
	 */
	@Autowired
	private UserDetailsService customUserDetailService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().
		antMatchers("/resources/**").permitAll()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/reg**").permitAll()
		.antMatchers("/admin**").hasRole("ADMIN")
				.anyRequest().authenticated().and().formLogin().loginPage("/login")
				.successForwardUrl("/dashboard").permitAll().and()
            .logout()                                    
                .permitAll().and().csrf().disable();

		// H2 console
		http.headers().frameOptions().sameOrigin();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * @Bean public DaoAuthenticationProvider authProvider() {
	 * DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	 * authProvider.setUserDetailsService(customUserDetailService);
	 * authProvider.setPasswordEncoder(new BCryptPasswordEncoder()); return
	 * authProvider; }
	 */

	/*
	 * @Bean public PasswordEncoder passwordEncoder() { PasswordEncoder encoder =
	 * new BCryptPasswordEncoder(); return NoOpPasswordEncoder.getInstance();
	 * PasswordEncoderFactories.createDelegatingPasswordEncoder(); return encoder; }
	 */
}