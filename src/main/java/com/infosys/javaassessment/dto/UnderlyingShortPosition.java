package com.infosys.javaassessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
public class UnderlyingShortPosition {

	private String date;
	
	private float shortPercent;
	
	private long shares;
	
	private String positionHolder;
	
}
