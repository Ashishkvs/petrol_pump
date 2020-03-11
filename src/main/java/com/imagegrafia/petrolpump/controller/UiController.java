package com.imagegrafia.petrolpump.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.imagegrafia.petrolpump.entity.GraphData;
import com.imagegrafia.petrolpump.entity.Nozzle;
import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.entity.Totalizer;
import com.imagegrafia.petrolpump.entity.UserAccount;
import com.imagegrafia.petrolpump.repository.NozzleRepository;
import com.imagegrafia.petrolpump.repository.PumpRepository;
import com.imagegrafia.petrolpump.repository.UserRepository;
import com.imagegrafia.petrolpump.service.NozzleService;
import com.imagegrafia.petrolpump.service.PumpService;
import com.imagegrafia.petrolpump.service.TotalizerService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author ashish
 *
 */
@Controller
@RequestMapping("/ui")
@Slf4j
public class UiController {

	@Autowired
	private TotalizerService totalizerService;

	@Autowired
	PumpRepository pumpRepository;

	@Autowired
	NozzleRepository nozzleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NozzleService nozzleService;
	private boolean enableMessage;

	private static Pump pump;
	private static Nozzle nozzle = new Nozzle();

	@GetMapping("/")
	public String index(@AuthenticationPrincipal Principal principal, Model model) {
		model.addAttribute("totalizer", new Totalizer());
		model.addAttribute("prevDayVolume", "");
		model.addAttribute("prevDayAmount", "");
		model.addAttribute("principal", principal.getName());
//		totalizerService.getPreviousDayTotalizer();
		return "redirect:/ui/dashboard";
	}

	@GetMapping("/login/{id}")
	public ModelAndView uiLogin(@PathVariable("id") Integer pumpId, ModelMap modelMap) {
		Optional<Pump> pumpById = pumpRepository.findById(pumpId);
		pump = pumpById.get();
//		return "dashboard";
//		modelMap.addAttribute("pump", pumpById.get());
		return new ModelAndView("forward:/ui/dashboard", modelMap);
	}

	@PostMapping("/saveRecords")
	public String saveRecord(@ModelAttribute @Valid Totalizer totalizer, BindingResult bindingResult,
			HttpServletResponse response, Model model) {
//		totalizerService.validateNewData(totalizer);
//		log.info("Totalizer :: {} ", totalizer);
		totalizer.setNozzle(nozzle);
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

	// table ui
	@GetMapping("/{nozzleId}/table")
	public String tableUi(@PathVariable("nozzleId") int nozzleId, Model model) {
		List<Totalizer> totalizerLists = totalizerService.findByNozzle(nozzleId);
		log.info("totalizerService.findAllTotalizer() Size ::  {}", totalizerLists.size());
		// sort data based on dates
		totalizerLists.sort(totalizerService.comparator.reversed());
		List<GraphData> graphDatas = getGraphData(totalizerLists);
		model.addAttribute("graphDatas", graphDatas);
		model.addAttribute("totalizerLists", totalizerLists);

		// for Totalizer form need nozzle id

		nozzle.setId(nozzleId);

//		log.info("nozzle id "+ nozzleId);
//		Totalizer totalizer = new Totalizer();
//		totalizer.setTempVar(150);
//		totalizer.setNozzle(nozzle);

		model.addAttribute("totalizer", new Totalizer());
		model.addAttribute("prevDayVolume", "");
		model.addAttribute("prevDayAmount", "");
//		totalizerService.getPreviousDayTotalizer();
//		return "index";

		return "tableView";
	}

	// dashboard
	@Autowired
	PumpService pumpService;

	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		String name = principal.getName();
		UserAccount userAccount = findUserAccountByPrincipal(principal);
		Optional<Pump> pumpById = pumpRepository.findById(userAccount.getId());
		if (pumpById == null) {
			pump = new Pump();
		} else {
			pump = pumpById.get();
		}
		intializeDashboard(model);
		return "dashboard";
	}

	private UserAccount findUserAccountByPrincipal(Principal principal) {
		// TODO Auto-generated method stub
		UserAccount findByEmail = userRepository.findByEmail(principal.getName());
		return findByEmail;

	}

	@PostMapping("/nozzleRecord")
	public String saveNozzle(@ModelAttribute Nozzle nozzle, Model model) {
		intializeDashboard(model);
		// enable success or failed message
		this.enableMessage = true;

		model.addAttribute("message", nozzle.getName().toUpperCase() + " saved successfully");
		log.info("Nozzle : {}", nozzle);
		if (nozzle != null && this.pump != null) {
			nozzleService.saveNozzle(nozzle, pump);
		}
		log.info("AllNozzle:::::: : {}", nozzleService.findAllNozzleByPumpId(1));
		return "dashboard";
	}

	private List<GraphData> getGraphData(List<Totalizer> totalizers) {
		List<GraphData> graphDatas = new ArrayList<>();
		int i = 1;
		for (Totalizer totalizer : totalizers) {
			double dayAmount = totalizer.getDayEndAmount() - totalizer.getDayStartAmount();
			double dayVolume = totalizer.getDayEndVolume() - totalizer.getDayStartVolume();
			int dayNo = i++;
			GraphData graphData = new GraphData(dayNo, dayVolume, dayAmount);
			graphDatas.add(graphData);
		}
		log.info("graphDatas ::  {}", graphDatas);
		return graphDatas;
	}

	// to make view constant for all http request
	private void intializeDashboard(Model model) {
		List<Pump> pumps = (List<Pump>) pumpRepository.findAll();
//		model.addAttribute("pump", pumps.get(0));
		model.addAttribute("pump", this.pump);
		List<Nozzle> nozzles = (List<Nozzle>) nozzleRepository.findByPump(this.pump);
		model.addAttribute("nozzles", nozzles);
		// for empty form
		model.addAttribute("nozzle", new Nozzle());

		model.addAttribute("enableMessage", enableMessage);
		model.addAttribute("allTypes", new String[] { "anyDay", "today" });
	}

}
