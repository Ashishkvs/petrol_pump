package com.imagegrafia.petrolpump.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.imagegrafia.petrolpump.entity.Nozzle;
import com.imagegrafia.petrolpump.entity.Totalizer;
import com.imagegrafia.petrolpump.entity.TotalizerDTO;
import com.imagegrafia.petrolpump.exception.InvalidDataException;
import com.imagegrafia.petrolpump.repository.NozzleRepository;
import com.imagegrafia.petrolpump.repository.TotalizerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TotalizerService {
	private static final String TOTALIZER_SERVICE = "TotalizerService ->()";
	@Autowired
	private TotalizerRepository totalizerRepository;

	@Autowired
	private NozzleRepository nozzleRepository;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tempDate = new Date();

	public List<Totalizer> findAllTotalizer() {
		return (List<Totalizer>) totalizerRepository.findAll();
	}

	public Totalizer findByIdTotalizer(int totalizerId) {
		return totalizerRepository.findById(totalizerId).get();
	}

	public List<Totalizer> findByNozzle(int nozzleId) {
		Optional<Nozzle> nozzle = nozzleRepository.findById(nozzleId);
		nozzle.orElseThrow(() -> new InvalidDataException("Nozzle id :" + nozzleId + " doesn't exists"));

		return (List<Totalizer>) totalizerRepository.findByNozzle(nozzle.get());
	}

	public Totalizer saveTotalizer(TotalizerDTO totalizerDTO, MultipartFile file) {
		Optional<Nozzle> findNozzleById = nozzleRepository.findById(totalizerDTO.getNozzleId());
		findNozzleById.orElseThrow(() -> new InvalidDataException("Nozzle id :" + totalizerDTO.getNozzleId()));

		Totalizer totalizer = convertToTotalizer(totalizerDTO);
		if (totalizer.getType().equals("today")) {
			totalizer.setCreatedDate(getDateWithZeroTime());
			// check previous day data available
			Totalizer previousDayTotalizer = getPreviousDayTotalizer(totalizer.getNozzle(), totalizer.getCreatedDate());
			if (previousDayTotalizer != null) {
				totalizer.setDayStartAmount(previousDayTotalizer.getDayEndAmount());
				totalizer.setDayStartVolume(previousDayTotalizer.getDayEndVolume());
			}

		}
		validateTotalizerData(totalizer);

		// if multiple updates are happening for same day then just update existing data
		List<Totalizer> totalizerList = totalizerRepository.findByCreatedDate(totalizer.getCreatedDate());
		if (!totalizerList.isEmpty()) {
			Totalizer totalizer2 = totalizerList.get(0);
			totalizer.setId(totalizer2.getId());
			// delete existing file during multiple update
			File fileUrl = new File("src/main/resources/static/upload/" + totalizer2.getFileUrl());
			if (totalizer2.getFileUrl() != null && fileUrl.exists()) {
				fileUrl.delete();
				log.info("Delete file : {}", totalizer2.getFileUrl());
			}
		}
		// fileupload
		if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
			totalizer.setFileUrl(storeFile(file));
		}

		return totalizerRepository.save(totalizer);
	}

	private String storeFile(MultipartFile file) {
		// TO DO compress file
		String name = System.currentTimeMillis()
				+ file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4);
		try {
			byte[] bytes = file.getBytes();

			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/upload/" + name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	private Totalizer convertToTotalizer(TotalizerDTO totalizerDTO) {
		Totalizer totalizer = new Totalizer();
		totalizer.setCreatedDate(totalizerDTO.getCreatedDate());
		totalizer.setDayEndAmount(totalizerDTO.getDayEndAmount());
		totalizer.setDayEndVolume(totalizerDTO.getDayEndVolume());
		totalizer.setDayStartAmount(totalizerDTO.getDayStartAmount());
		totalizer.setDayStartVolume(totalizerDTO.getDayStartVolume());
		totalizer.setType(totalizerDTO.getType());
		Nozzle nozzle = new Nozzle();
		nozzle.setId(totalizerDTO.getNozzleId());
		totalizer.setNozzle(nozzle);
		return totalizer;

	}

	public Totalizer updateTotalizer(int totalizerId, Totalizer totalizer) {
		totalizer.setId(totalizerId);
		return totalizerRepository.save(totalizer);
	}

	public Totalizer getPreviousDayTotalizer(Nozzle nozzle, Date date) {
		// temp optimise using query
		log.info("Date {} ", date);
//		List<Totalizer> findAllTotalizer = findAllTotalizer().stream().filter( data -> data.getCreatedDate().before(date)).collect(Collectors.toList());

		List<Totalizer> findAllTotalizer = totalizerRepository.findByNozzle(nozzle);
		log.info("FindAllTotalizerBYNOZZLE {} ", findAllTotalizer.size());

		if (!findAllTotalizer.isEmpty()) {
			findAllTotalizer = findAllTotalizer.stream().filter(data -> data.getCreatedDate().before(date))
					.collect(Collectors.toList());
		}

		// after filter again check list is empty ?
		if (findAllTotalizer.isEmpty()) {
			return null;
		}

		log.info("Filetr totalizer before createdDate: {} ", findAllTotalizer);
		findAllTotalizer.sort(comparator.reversed());
		log.info("from service :: {} ", findAllTotalizer.get(0));
		return findAllTotalizer.get(0);

	}

	public void validateTotalizerData(Totalizer totalizer) {
		log.info(TOTALIZER_SERVICE + "validateNewData");
		// validate date
		if (totalizer == null) {
			throw new InvalidDataException("Totalizer cannot be null");
		}
		if (totalizer.getCreatedDate().after(new Date())) {
			throw new InvalidDataException("date cannot be further than today : " + new Date());
		}
		// check previous day data
		Totalizer previousDayTotalizer = getPreviousDayTotalizer(totalizer.getNozzle(), totalizer.getCreatedDate());
		// if previous date data not available
		if (previousDayTotalizer == null) {

			return;
		}

		if (totalizer.getDayEndAmount() < previousDayTotalizer.getDayEndAmount()) {
			throw new InvalidDataException("Invalid Data Passed currentTotalizerAmount " + totalizer.getDayEndAmount()
					+ " is less than previousDayTotalizerAmount " + previousDayTotalizer.getDayEndAmount());
		}
		if (totalizer.getDayEndVolume() < previousDayTotalizer.getDayEndVolume()) {
			throw new InvalidDataException("Invalid Data Passed currentTotalizerVolume " + totalizer.getDayEndVolume()
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

	private Date getDateWithZeroTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
