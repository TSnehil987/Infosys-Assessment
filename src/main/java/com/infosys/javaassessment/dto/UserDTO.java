package com.infosys.javaassessment.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
public class UserDTO implements Serializable{
	
	private static final long serialVersionUID = -3410879369095844752L;

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String userId;
	
	private String encryptedPassword;
}
