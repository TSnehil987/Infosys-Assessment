package com.infosys.javaassessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
public class CreateUserResponseModel {
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String userId;
}
