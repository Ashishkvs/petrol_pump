package com.imagegrafia.petrolpump.entity;

import java.util.Date;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.ToString;

@Data
public class TotalizerDTO {
	private double dayStartVolume;
	private double dayStartAmount;
	private double dayEndVolume;
	private double dayEndAmount;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdDate;
	private String type;
	private int nozzleId;
}
