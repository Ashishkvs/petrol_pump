package com.imagegrafia.petrolpump.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Nozzle {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotNull
	@NotBlank(message="nozzle name cannot be null or empty")
	private String name;
	@NotNull
	@NotBlank(message="nozzle type cannot be null or empty")
	private String type;
}
