package com.imagegrafia.petrolpump.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.entity.UserAccount;
import com.imagegrafia.petrolpump.repository.PumpRepository;
import com.imagegrafia.petrolpump.service.UserAccountService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("ui/pump")
@Slf4j
public class PumpController {

	@Autowired
	PumpRepository pumpRepository;
	
	@Autowired
	private UserAccountService userAccountService;

	@PostMapping
	String createPump(Principal principal,@ModelAttribute Pump pump,Model model) {
		log.info("UserName : {}",principal.getName());
		intializePetorlPump(model);
		UserAccount userAccount = userAccountService.findUserAccountByPrincipal(principal);
		pump.setUserAccount(userAccount);
		Pump pump2 = pumpRepository.save(pump);
		log.info("Pump Created : {} ",pump2);
		return "redirect:/ui/dashboard";
	}

	@GetMapping
	String getPumpById(Long id,Model model){
		intializePetorlPump(model);
		return "pump";
	}

	private void intializePetorlPump(Model model) {
		List<Pump> pumps = (List<Pump>) pumpRepository.findAll();
//		model.addAttribute("pump", pumps.get(0));
		model.addAttribute("pumpForm", new Pump());
	}
	
}
