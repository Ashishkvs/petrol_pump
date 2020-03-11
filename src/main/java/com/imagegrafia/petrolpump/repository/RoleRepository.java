package com.imagegrafia.petrolpump.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imagegrafia.petrolpump.entity.Role;


@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
