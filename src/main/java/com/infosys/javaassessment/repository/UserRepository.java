package com.infosys.javaassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infosys.javaassessment.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>  {

	UserEntity findByEmail(String email); 
}
