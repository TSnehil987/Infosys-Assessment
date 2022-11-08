package com.infosys.javaassessment.service;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infosys.javaassessment.dto.UserDTO;
import com.infosys.javaassessment.entity.UserEntity;
import com.infosys.javaassessment.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/*
	 * Method to save user in database
	 * 
	 * @param userDetails
	 * 
	 * @return UserEntity
	 */
	@Override
	public UserEntity createUser(UserDTO userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		return userRepository.save(userEntity);
	}

	/*
	 * Method to load user by username provided by spring security for user
	 * authentication
	 * 
	 * @param username
	 * 
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);
		if (userEntity == null)
			throw new UsernameNotFoundException(username);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	/*
	 * Method to get user by email
	 * 
	 * @param email
	 * 
	 * @return UserDTO
	 */
	@Override
	public UserDTO getUserByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		return new ModelMapper().map(userEntity, UserDTO.class);
	}
}
