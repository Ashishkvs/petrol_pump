package com.imagegrafia.petrolpump.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.exception.InvalidDataException;
import com.imagegrafia.petrolpump.repository.NozzleRepository;
import com.imagegrafia.petrolpump.repository.PumpRepository;
import com.imagegrafia.petrolpump.repository.TotalizerRepository;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PumpService {
	private static final String PUMP_SERVICE = "PumpService ->()";
	@Autowired
	private PumpRepository pumpRepository;
	

	public List<Pump> findAllPump() {
		return (List<Pump>) pumpRepository.findAll();
	}
	
	public Pump findPumpById(int pumpId) {
		return pumpRepository.findById(pumpId).get();
	}

	public Pump saveNozzle(Pump pump) {
		
		return pumpRepository.save(pump);
	}

	public Pump updatePump(int pumpId, Pump pump) {
		pump.setId(pumpId);
		return pumpRepository.save(pump);
	}

}
