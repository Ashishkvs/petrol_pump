package com.imagegrafia.petrolpump;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PetrolPumpApplication extends SpringBootServletInitializer{
	

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(PetrolPumpApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(PetrolPumpApplication.class, args);
	}

}
