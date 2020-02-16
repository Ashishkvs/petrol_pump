package com.imagegrafia.petrolpump.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imagegrafia.petrolpump.entity.Totalizer;
import com.imagegrafia.petrolpump.repository.TotalizerRepository;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	public Totalizer updateTotalizer(int totalizerId, Totalizer totalizer) {
		totalizer.setId(totalizerId);
		return totalizerRepository.save(totalizer);
	}

	public Totalizer getPreviousDayTotalizer() {
		Comparator<Totalizer> comparator =new Comparator<Totalizer>() {

			@Override
			public int compare(Totalizer o1, Totalizer o2) {
				if(o1.getCreatedDate().compareTo(o2.getCreatedDate()) > 0)
				return 1;
				else if(o1.getCreatedDate().compareTo(o2.getCreatedDate()) < 0)
						return -1;
				else return 0;
			}
			
		};
		List<Totalizer> findAllTotalizer = findAllTotalizer();
		findAllTotalizer.sort(comparator.reversed());
		log.info("from service :: {} ",findAllTotalizer.get(0));
		if(findAllTotalizer.isEmpty())
		return null;
		else return findAllTotalizer.get(0);
	}
	public void validateNewData(Totalizer currentTotalizer){
		Totalizer previousDayTotalizer = getPreviousDayTotalizer();
		if(currentTotalizer.getDayEndAmount() < previousDayTotalizer.getDayEndAmount()) {
			throw new RuntimeException( "Invalid Data Passed currentTotalizerAmount "
					+currentTotalizer.getDayEndAmount()+ " is less than previousDayTotalizerAmount "
							+ previousDayTotalizer.getDayEndAmount()) ;
		}
		if(currentTotalizer.getDayEndVolume() < previousDayTotalizer.getDayEndVolume()) {
			throw new RuntimeException( "Invalid Data Passed currentTotalizerVolume "
					+currentTotalizer.getDayEndVolume()+ " is less than previousDayTotalizerVolume "
							+ previousDayTotalizer.getDayEndVolume()) ;
		}
	}

}
