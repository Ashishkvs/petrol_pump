package com.imagegrafia.petrolpump.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imagegrafia.petrolpump.entity.UserAccount;

@Repository
public interface UserRepository extends CrudRepository<UserAccount, Integer> {

	UserAccount findByEmail(String email);

}
