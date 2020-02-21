package com.imagegrafia.petrolpump.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.ToString;

@Entity
@ToString(exclude = "nozzle")
@Data
public class Totalizer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private double dayStartVolume;
	private double dayStartAmount;
	private double dayEndVolume;
	private double dayEndAmount;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdDate;

	@ManyToOne
	private Nozzle nozzle;
}
