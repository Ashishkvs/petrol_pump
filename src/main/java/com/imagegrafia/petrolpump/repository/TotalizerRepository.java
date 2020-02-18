package com.imagegrafia.petrolpump.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imagegrafia.petrolpump.entity.Totalizer;

@Repository
public interface TotalizerRepository extends CrudRepository<Totalizer, Integer>{

	List<Totalizer> findByCreatedDate(Date createdDate);

}
