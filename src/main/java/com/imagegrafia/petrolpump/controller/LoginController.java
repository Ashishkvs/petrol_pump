package com.imagegrafia.petrolpump.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.imagegrafia.petrolpump.entity.UserAccount;
import com.imagegrafia.petrolpump.service.UserAccountService;

import lombok.extern.slf4j.Slf4j;

/**
 * Use default login-page and logout for Login given by spring security
 */
@Controller
@Slf4j
public class LoginController {
	@Autowired
	UserAccountService userService;

	@GetMapping("/")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		return "index";
	}

	@GetMapping("/reg")
	public String regForm(Model model) {
		model.addAttribute("user", new UserAccount());
		return "user";
	}
	
	@GetMapping("/admin")
	public String adminForm(Model model) {
		model.addAttribute("user", new UserAccount());
		return "admin";
	}

	@PostMapping("/reg")
	public String userRegForm(@ModelAttribute UserAccount user, Model model) {
		model.addAttribute("user", new UserAccount());
		log.info("UserAccount obj: {} ", user);
		userService.createUser(user);
		return "user";
	}
	
	/**
	 * 
	 * Custom Login controller
	 */

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
//	@PostMapping("/login")
//	public String loginPageSubmit(@ModelAttribute Object obj) {
//		log.info("MOdel object :  {}",obj);
//		return "index";
//	}
}
