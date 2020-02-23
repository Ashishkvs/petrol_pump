package com.imagegrafia.petrolpump.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imagegrafia.petrolpump.entity.Nozzle;
import com.imagegrafia.petrolpump.entity.Totalizer;
import com.imagegrafia.petrolpump.exception.InvalidDataException;
import com.imagegrafia.petrolpump.repository.NozzleRepository;
import com.imagegrafia.petrolpump.repository.TotalizerRepository;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TotalizerService {
	private static final String TOTALIZER_SERVICE = "TotalizerService ->()";
	@Autowired
	private TotalizerRepository totalizerRepository;
	
	@Autowired
	private NozzleRepository nozzleRepository;

	public List<Totalizer> findAllTotalizer() {
		return (List<Totalizer>) totalizerRepository.findAll();
	}

	public Totalizer findByIdTotalizer(int totalizerId) {
		return totalizerRepository.findById(totalizerId).get();
	}
	public List<Totalizer> findByNozzle(int nozzleId){
		Optional<Nozzle> nozzle = nozzleRepository.findById(nozzleId);
		nozzle.orElseThrow( () -> new InvalidDataException("Nozzle id :"+nozzleId+" doesn't exists"));
		
		return (List<Totalizer>) totalizerRepository.findByNozzle(nozzle.get());
	}
	public Totalizer saveTotalizer(Totalizer totalizer) {
		
		validateNewData(totalizer);
		//if multiple updates are happening for same day then just update exisitng data
		List<Totalizer> findByCreatedDate = totalizerRepository.findByCreatedDate(totalizer.getCreatedDate());
		if(findByCreatedDate !=null && !findByCreatedDate.isEmpty()) {
			Totalizer totalizer2 = findByCreatedDate.get(0);
			totalizer.setId(totalizer2.getId());
		}
		return totalizerRepository.save(totalizer);
	}

	public Totalizer updateTotalizer(int totalizerId, Totalizer totalizer) {
		totalizer.setId(totalizerId);
		return totalizerRepository.save(totalizer);
	}

	public Totalizer getPreviousDayTotalizer(Date date) {
		//temp optimise using query
		List<Totalizer> findAllTotalizer = findAllTotalizer().stream().filter( data -> data.getCreatedDate().before(date)).collect(Collectors.toList());
		log.info("from service :findAllTotalizer: {} ", findAllTotalizer);
		if (findAllTotalizer.isEmpty())
			return null;
		else {
			log.info("from service :: {} ", findAllTotalizer.get(0));
			findAllTotalizer.sort(comparator.reversed());
			return findAllTotalizer.get(0);
		}
	}

	public void validateNewData(Totalizer currentTotalizer) {
		//validate date
				if(currentTotalizer.getCreatedDate().after(new Date())) {
					throw new InvalidDataException("date cannot be further than today : "+new Date());
				}
		log.info(TOTALIZER_SERVICE + "validateNewData");
		Totalizer previousDayTotalizer = getPreviousDayTotalizer(currentTotalizer.getCreatedDate());
		if(previousDayTotalizer == null) {
			//if previous date data not available
			return;
		}
		if (currentTotalizer.getDayEndAmount() < previousDayTotalizer.getDayEndAmount()) {
			throw new InvalidDataException(
					"Invalid Data Passed currentTotalizerAmount " + currentTotalizer.getDayEndAmount()
							+ " is less than previousDayTotalizerAmount " + previousDayTotalizer.getDayEndAmount());
		}
		if (currentTotalizer.getDayEndVolume() < previousDayTotalizer.getDayEndVolume()) {
			throw new InvalidDataException(
					"Invalid Data Passed currentTotalizerVolume " + currentTotalizer.getDayEndVolume()
							+ " is less than previousDayTotalizerVolume " + previousDayTotalizer.getDayEndVolume());
		}
	}

	// Totalizer comparator to sort data by date
		public Comparator<Totalizer> comparator = new Comparator<Totalizer>() {

			@Override
			public int compare(Totalizer o1, Totalizer o2) {
				if (o1.getCreatedDate().compareTo(o2.getCreatedDate()) > 0)
					return 1;
				else if (o1.getCreatedDate().compareTo(o2.getCreatedDate()) < 0)
					return -1;
				else
					return 0;
			}

		};
}
