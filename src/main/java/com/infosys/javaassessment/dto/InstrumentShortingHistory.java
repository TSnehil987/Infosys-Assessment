package com.infosys.javaassessment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
public class InstrumentShortingHistory {

	private String isin;
	
	private String issuerName;
	
	List<AggregatedShortEvent> events;
}
