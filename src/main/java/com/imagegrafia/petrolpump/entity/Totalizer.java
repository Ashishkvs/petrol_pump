package com.imagegrafia.petrolpump.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
