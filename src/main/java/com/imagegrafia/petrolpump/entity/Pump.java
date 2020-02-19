package com.imagegrafia.petrolpump.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
public class Pump {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotNull
	@NotBlank(message="pump name cannot be null or empty")
	private String name;
	
	@NotBlank(message="pump address cannot be null or empty")
	private String address;
	
	@OneToMany
	private Nozzle nozzles;
}
