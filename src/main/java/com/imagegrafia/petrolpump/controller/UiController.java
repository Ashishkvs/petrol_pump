package com.imagegrafia.petrolpump.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.imagegrafia.petrolpump.entity.GraphData;
import com.imagegrafia.petrolpump.entity.Nozzle;
import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.entity.Totalizer;
import com.imagegrafia.petrolpump.entity.TotalizerDTO;
import com.imagegrafia.petrolpump.entity.UserAccount;
import com.imagegrafia.petrolpump.exception.InvalidDataException;
import com.imagegrafia.petrolpump.repository.NozzleRepository;
import com.imagegrafia.petrolpump.repository.PumpRepository;
import com.imagegrafia.petrolpump.repository.UserRepository;
import com.imagegrafia.petrolpump.service.NozzleService;
import com.imagegrafia.petrolpump.service.PumpService;
import com.imagegrafia.petrolpump.service.TotalizerService;
import com.imagegrafia.petrolpump.service.UserAccountService;

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
	private NozzleService nozzleService;
	@Autowired
	private UserAccountService userAccountService;
	private boolean enableMessage;
	@Autowired
	PumpService pumpService;

	@GetMapping("/")
	public String index(@AuthenticationPrincipal Principal principal, Model model) {
		model.addAttribute("totalizer", new Totalizer());
		model.addAttribute("prevDayVolume", "");
		model.addAttribute("prevDayAmount", "");
		model.addAttribute("principal", principal.getName());
//		totalizerService.getPreviousDayTotalizer();
		return "redirect:/ui/dashboard";
	}


	@PostMapping("/totalizer")
	public String saveTotalizerRecord(@RequestParam("file") MultipartFile file,@ModelAttribute TotalizerDTO totalizerDTO, BindingResult bindingResult,
			HttpServletResponse response, Model model, Principal principal) {
//		log.info("file.getContentType() :: {} ", file.getOriginalFilename());
//		log.info("file.getContentType() :: {} ", file.getContentType());
		log.info("Totalizer :: {} ", totalizerDTO);
		totalizerService.saveTotalizer(totalizerDTO,file);
		return "redirect:/ui/" + totalizerDTO.getNozzleId() + "/table";
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

		TotalizerDTO totalizerDTO = new TotalizerDTO();

		Optional<Nozzle> nozzle = nozzleRepository.findById(nozzleId);
		if (nozzle.isPresent()) {
			totalizerDTO.setNozzleId(nozzleId);
			totalizerDTO.setCreatedDate(getDateWithZeroTime());

		} else {
			throw new InvalidDataException("Nozzle id :" + nozzleId + "Not found");
		}

		model.addAttribute("totalizer", totalizerDTO);
		model.addAttribute("prevDayVolume", "");
		model.addAttribute("prevDayAmount", "");
//		totalizerService.getPreviousDayTotalizer();
//		return "index";

		return "tableView";
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		String name = principal.getName();
		log.info("UserDetails : {}", name);

		// find id of current logged in User
		UserAccount userAccount = userAccountService.findUserAccountByPrincipal(principal);

		// find petrol pump
		Optional<Pump> pump = pumpService.getPumpByUserAccount(userAccount);

		// if petrol pump not found create one
		if (!pump.isPresent()) {
			return "forward:/ui/pump";
		}

		log.info("pump :{}", pump.get());

		List<Nozzle> nozzles = nozzleRepository.findByPump(pump.get());
		log.info("Nozzles :{} ", nozzles);

		model.addAttribute("pump", pump.get());
		model.addAttribute("nozzles", nozzles);

		intializeDashboard(model, principal);
		return "dashboard";
	}

	@PostMapping("/dashboard")
	public String saveNozzle(@ModelAttribute Nozzle nozzle, Model model, Principal principal) {

		UserAccount userAccount = userAccountService.findUserAccountByPrincipal(principal);
		Optional<Pump> pump = pumpService.getPumpByUserAccount(userAccount);

		Nozzle savedNozzle = nozzleService.saveNozzle(nozzle, pump.get());
		log.info("Nozzle : {}", savedNozzle);
		List<Nozzle> findAllNozzleByPump = nozzleService.findAllNozzleByPump(pump.get());
		log.info(pump.get().getName() + " : AllNozzle : {}", findAllNozzleByPump);

		// enable success or failed message
		this.enableMessage = true;

		model.addAttribute("message", nozzle.getName().toUpperCase() + " saved successfully");
		model.addAttribute("enableMessage", enableMessage);

		intializeDashboard(model, principal);
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
	private void intializeDashboard(Model model, Principal principal) {
		// for empty form
		UserAccount userAccount = userAccountService.findUserAccountByPrincipal(principal);
		Optional<Pump> pump = pumpService.getPumpByUserAccount(userAccount);

		List<Nozzle> findAllNozzleByPump = nozzleService.findAllNozzleByPump(pump.get());
		model.addAttribute("pump", pump.get());
		model.addAttribute("nozzles", findAllNozzleByPump);

//		model.addAttribute("message", enableMessage);
		// form reset
		model.addAttribute("nozzle", new Nozzle());
		model.addAttribute("allTypes", new String[] { "anyDay", "today" });
	}

	private Date getDateWithZeroTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
