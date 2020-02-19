package com.imagegrafia.petrolpump.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imagegrafia.petrolpump.entity.Nozzle;
import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.entity.Totalizer;

@Repository
public interface NozzleRepository extends CrudRepository<Nozzle, Integer>{

	List<Nozzle> findByPump(Pump pump);


}
