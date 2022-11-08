package com.infosys.javaassessment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.javaassessment.dto.InstrumentShortingHistory;
import com.infosys.javaassessment.exception.InvalidInputException;
import com.infosys.javaassessment.service.FinanstilsynetService;
import com.infosys.javaassessment.util.FinanstilsynetConstants;

@RestController
@RequestMapping("instruments")
public class FinanstilsynetRestController {
	
	Logger log = LoggerFactory.getLogger(FinanstilsynetRestController.class);

	@Autowired
	private FinanstilsynetService finanstilsynetService;
	
	@GetMapping("/{isin}/events")
	public ResponseEntity<List<InstrumentShortingHistory>> getInstrumentEvents(@PathVariable("isin") String isin, 
			@RequestParam(value="fromDate", required=false) String fromDate, 
			@RequestParam(value="toDate", required=false) String toDate) {
		log.debug("ISIN received: {}", isin);
		log.debug("fromDate: {}, toDate: {}", fromDate, toDate);
		List<InstrumentShortingHistory> list = null;
		if(!isin.isBlank()) {
			list = finanstilsynetService.getShortPosition(isin, fromDate, toDate);
		} else {
			throw new InvalidInputException(FinanstilsynetConstants.INVALID_ISIN_MSG);
		}
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
}
