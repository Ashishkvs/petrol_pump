package com.imagegrafia.petrolpump.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imagegrafia.petrolpump.entity.Nozzle;
import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.exception.InvalidDataException;
import com.imagegrafia.petrolpump.repository.NozzleRepository;
import com.imagegrafia.petrolpump.repository.PumpRepository;
import com.imagegrafia.petrolpump.repository.TotalizerRepository;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NozzleService {
	private static final String NOZZLE_SERVICE = "NozzleService ->()";
	@Autowired
	private NozzleRepository nozzleRepository;
	@Autowired
	private PumpRepository pumpRepository;

	public List<Nozzle> findAllNozzle() {
		return (List<Nozzle>) nozzleRepository.findAll();
	}

	public List<Nozzle> findAllNozzleByPump(Pump pump) {
		return (List<Nozzle>) nozzleRepository.findByPump(pump);
	}

	public Nozzle findByIdTotalizer(int totalizerId) {
		return nozzleRepository.findById(totalizerId).get();
	}

	public Nozzle saveNozzle(Nozzle nozzle, Pump pump) {
//		Optional<Pump> findById = pumpRepository.findById(pump.g);
		if (nozzle == null || pump == null) {
			throw new InvalidDataException( "Nozzle :"+nozzle+"Pump : "+pump); 
		}
			nozzle.setPump(pump);
		log.info(NOZZLE_SERVICE + " {}", nozzle);
		return nozzleRepository.save(nozzle);
	}

	public Nozzle updateNozzle(int totalizerId, Nozzle nozzle) {
		nozzle.setId(totalizerId);
		return nozzleRepository.save(nozzle);
	}

}
