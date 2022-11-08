package com.infosys.javaassessment.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.javaassessment.dto.UserDTO;
import com.infosys.javaassessment.model.CreateUserRequestModel;
import com.infosys.javaassessment.model.CreateUserResponseModel;
import com.infosys.javaassessment.service.UserService;


@RestController
@RequestMapping("/users")
public class UsersController {
	
	Logger log = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/status/check")
	public String status() {
		return "Working on port : " + env.getProperty("local.server.port");
	}
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, 
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
		log.debug("User Details: {}", userDetails);
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		var userDTO = modelMapper.map(userDetails, UserDTO.class);
		var createdUser = userService.createUser(userDTO);
		log.debug("User details after save: {}", createdUser);
		var returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);
		return new ResponseEntity<>(returnValue, HttpStatus.OK);
	}
}
