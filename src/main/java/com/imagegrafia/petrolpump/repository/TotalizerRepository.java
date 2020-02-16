package com.imagegrafia.petrolpump.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imagegrafia.petrolpump.entity.Totalizer;

@Repository
public interface TotalizerRepository extends CrudRepository<Totalizer, Integer>{

}
