package com.imagegrafia.petrolpump.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.imagegrafia.petrolpump.entity.Totalizer;

@Controller
@RequestMapping("/ui")
public class UiController {
	@GetMapping("/")
	public String index(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		Totalizer totalizer = new Totalizer(1,10.0,500.5,100.0,5000.0,new Date());
		model.addAttribute("totalizer", totalizer);
		return "index";
	}
}
