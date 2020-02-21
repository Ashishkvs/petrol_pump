package com.imagegrafia.petrolpump.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"pump","totalizer"})
@Entity
public class Nozzle {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotNull
	@NotBlank(message="nozzle name cannot be null or empty")
	private String name;
	@NotNull
	@NotBlank(message="nozzle type cannot be null or empty")
	private String type;
	
	@NotNull(message="pump cannot be null")
	@ManyToOne
	@JoinColumn(name = "PUMP_ID")
	private Pump pump;
	
	@OneToMany(mappedBy="nozzle")
	private List<Totalizer> totalizer;
}
