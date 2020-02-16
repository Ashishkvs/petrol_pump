package com.imagegrafia.petrolpump.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.imagegrafia.petrolpump.entity.GraphData;
import com.imagegrafia.petrolpump.entity.Totalizer;
import com.imagegrafia.petrolpump.service.TotalizerService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/ui")
@Slf4j
public class UiController {
	@Autowired
	private TotalizerService totalizerService;

	@GetMapping("/")
	public String index(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
//		Totalizer totalizer = new Totalizer(1, 10.0, 500.5, 100.0, 5000.0, new Date());
		model.addAttribute("totalizer", new Totalizer(0, 0.0, 0.0, 0.0, 0.0, new Date()));
		return "index";
	}

	@PostMapping("/saveRecords")
	public String saveRecord(@ModelAttribute Totalizer totalizer, BindingResult bindingResult,
			HttpServletResponse response, Model model) {
		log.info("Totalizer :: {} ", totalizer);
		totalizerService.saveTotalizer(totalizer);
		return "index";
	}

	// graph ui
	@GetMapping("/graph")
	public String graphUi(Model model) {
		List<Totalizer> findAllTotalizer = totalizerService.findAllTotalizer();
		log.info("totalizerService.findAllTotalizer() ::  {}", findAllTotalizer);
		
		List<GraphData> graphDatas = getGraphData(findAllTotalizer);
		model.addAttribute("graphDatas", graphDatas);
		return "graph";
	}

	private List<GraphData> getGraphData(List<Totalizer> totalizers) {
		List<GraphData> graphDatas = new ArrayList<>();
		int i=1;
		for (Totalizer totalizer : totalizers) {
			double dayAmount = totalizer.getDayEndAmount() - totalizer.getDayStartAmount();
			double dayVolume = totalizer.getDayEndVolume() - totalizer.getDayStartVolume();
			int dayNo = i++;
			GraphData graphData = new GraphData(dayNo,dayVolume,dayAmount);
			graphDatas.add(graphData);
		}
		log.info("graphDatas ::  {}", graphDatas);
		return graphDatas;
	}
}
