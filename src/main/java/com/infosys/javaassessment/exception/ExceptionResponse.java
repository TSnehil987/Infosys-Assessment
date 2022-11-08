package com.infosys.javaassessment.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
public class ExceptionResponse {

	private Date timestamp;
	
	private String message;
	
	private String details;
	
}
