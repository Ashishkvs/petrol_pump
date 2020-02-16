package com.imagegrafia.petrolpump.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GraphData {

	private int dayNo;
	private double dayVolume;
	private double dayAmount;
}
