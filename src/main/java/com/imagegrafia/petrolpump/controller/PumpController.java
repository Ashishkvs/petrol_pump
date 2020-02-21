package com.imagegrafia.petrolpump.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.repository.PumpRepository;

@Controller
@RequestMapping("ui/pump")
public class PumpController {

	@Autowired
	PumpRepository pumpRepository;

	@PostMapping
	String createPump(@ModelAttribute Pump pump,Model model) {
		Pump pump1 = pumpRepository.save(pump);
		intializePetorlPump(model);
		return "pump";
	}

	@GetMapping
	String getPumpById(Long id,Model model){
		intializePetorlPump(model);
		return "pump";
	}

	private void intializePetorlPump(Model model) {
		List<Pump> pumps = (List<Pump>) pumpRepository.findAll();
		model.addAttribute("pump", pumps.get(0));
		model.addAttribute("pumpForm", new Pump());
	}

}
