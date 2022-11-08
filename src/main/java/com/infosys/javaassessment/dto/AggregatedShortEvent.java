package com.infosys.javaassessment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
public class AggregatedShortEvent {
	
	private String date;
	
	private float shortPercent;
	
	private long shares;
	
	private List<UnderlyingShortPosition> activePositions;
	
}
