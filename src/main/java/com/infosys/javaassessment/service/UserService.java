package com.infosys.javaassessment.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.infosys.javaassessment.dto.UserDTO;
import com.infosys.javaassessment.entity.UserEntity;

public interface UserService extends UserDetailsService {

	UserEntity createUser(UserDTO userDetails);
	
	UserDTO getUserByEmail(String email);
}
