package com.imagegrafia.petrolpump.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.imagegrafia.petrolpump.entity.Totalizer;
import com.imagegrafia.petrolpump.service.TotalizerService;

@RestController
@RequestMapping("/api")
public class TotalizerController {
	@Autowired
	private TotalizerService totalizerService;

	@GetMapping
	public ResponseEntity<List<Totalizer>> getAllTotalizer() {

		return new ResponseEntity<>(totalizerService.findAllTotalizer(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Totalizer> getTotalizerById(@PathVariable("id") int totalizerId) {
		Totalizer totalizer = new Totalizer();
		totalizer.setCreatedDate(new Date());
		return new ResponseEntity<>(totalizer, HttpStatus.OK);
	}

//	@PostMapping
//	public ResponseEntity<Totalizer> saveTotalizer(@RequestBody Totalizer totalizer) {
//		Totalizer saveTotalizer = totalizerService.saveTotalizer(totalizer);
//		return new ResponseEntity<Totalizer>(saveTotalizer, HttpStatus.CREATED);
//	}

	private HttpHeaders getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		return httpHeaders;
	}

	// disabled [NumberFormatException: For input string: "favicon.ico"]]
	@GetMapping("favicon.ico")
	@ResponseBody
	void returnNoFavicon() {
	}
}
