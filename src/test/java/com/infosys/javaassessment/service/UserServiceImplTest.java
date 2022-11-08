package com.infosys.javaassessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.infosys.javaassessment.dto.UserDTO;
import com.infosys.javaassessment.entity.UserEntity;
import com.infosys.javaassessment.repository.UserRepository;

@SpringBootTest
class UserServiceImplTest {
	
	private static final String EMAIL_TEXT = "stanya123@gmail.com";
	
	@InjectMocks
	UserServiceImpl userServiceImpl;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	ModelMapper modelMapper;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private UserDTO preparUserDTO() {
		return UserDTO.of("Tanya", "Snehil", EMAIL_TEXT, "Tanya@123", null, null);
	}
	
	private UserEntity preparUserEntity() {
		return UserEntity.of(Long.valueOf(1L), "Tanya", "Snehil", EMAIL_TEXT, 
				"c8ed6d20-8e90-4759-bce6-0697abdb7bf5", "$2a$10$da3kFP.u1Q/jbPaPfx/R2eD1wW7JpRnEO7l2jZAgQZXcrmaell.Vu");
	}
	
	@Test
	void loadUserByUsernameTest() {
		when(userRepository.findByEmail(EMAIL_TEXT)).thenReturn(preparUserEntity());
		var result = userServiceImpl.loadUserByUsername(EMAIL_TEXT);
		assertEquals(EMAIL_TEXT, result.getUsername());
	}

	@Test
	void loadUserByUsernameTest_ThrowsException() {
		when(userRepository.findByEmail(EMAIL_TEXT)).thenReturn(null);
		UsernameNotFoundException invalidInputExc = assertThrows(UsernameNotFoundException.class, 
				() -> userServiceImpl.loadUserByUsername(EMAIL_TEXT));
		assertEquals(EMAIL_TEXT, invalidInputExc.getMessage());
	}
	
	@Test
	void getUserByEmailTest() {
		when(userRepository.findByEmail(EMAIL_TEXT)).thenReturn(preparUserEntity());
		var result = userServiceImpl.getUserByEmail(EMAIL_TEXT);
		assertEquals(EMAIL_TEXT, result.getEmail());
	}
	
	@Test
	void createUserTest() {
		when(modelMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
		.thenReturn(preparUserEntity());
		when(userRepository.save(preparUserEntity())).thenReturn(preparUserEntity());
		var result = userServiceImpl.createUser(preparUserDTO());
		assertNull(result);
	}

	@Test
	void getUserByEmailTest_ThrowsException() {
		when(userRepository.findByEmail(EMAIL_TEXT)).thenReturn(null);
		UsernameNotFoundException invalidInputExc = assertThrows(UsernameNotFoundException.class, 
				() -> userServiceImpl.getUserByEmail(EMAIL_TEXT));
		assertEquals(EMAIL_TEXT, invalidInputExc.getMessage());
	}
}
