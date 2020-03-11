package com.imagegrafia.petrolpump.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imagegrafia.petrolpump.entity.Pump;
import com.imagegrafia.petrolpump.entity.UserAccount;

@Repository
public interface PumpRepository extends CrudRepository<Pump, Integer>{
	
	Optional<Pump> findByUserAccount(UserAccount userAccount);



}
