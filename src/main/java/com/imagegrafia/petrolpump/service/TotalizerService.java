package com.imagegrafia.petrolpump.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imagegrafia.petrolpump.entity.Totalizer;
import com.imagegrafia.petrolpump.repository.TotalizerRepository;

@Service
public class TotalizerService {
	@Autowired
	private TotalizerRepository totalizerRepository;
	
	public List<Totalizer> findAllTotalizer() {
		return (List<Totalizer>) totalizerRepository.findAll();
	}
	public Totalizer findByIdTotalizer(int totalizerId) {
		return totalizerRepository.findById(totalizerId).get();
	}
	
	public Totalizer saveTotalizer(Totalizer totalizer) {
		return totalizerRepository.save(totalizer);
	}
	public Totalizer updateTotalizer(int totalizerId , Totalizer totalizer) {
		totalizer.setId(totalizerId);
		return totalizerRepository.save(totalizer);
	}
	

}
