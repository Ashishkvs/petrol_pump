package com.imagegrafia.petrolpump.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "nozzles")
@Entity
public class Pump {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@NotBlank(message = "pump name cannot be null or empty")
	private String name;

	@NotBlank(message = "pump address cannot be null or empty")
	private String address;

	@OneToMany(cascade = CascadeType.ALL,mappedBy = "pump")
	private List<Nozzle> nozzles;
}
