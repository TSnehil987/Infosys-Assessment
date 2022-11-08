package com.infosys.javaassessment.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.infosys.javaassessment.dto.InstrumentShortingHistory;
import com.infosys.javaassessment.exception.TechnicalException;

@Component
public class FinanstilsynetUtils {
	
	private Logger log = LoggerFactory.getLogger(FinanstilsynetUtils.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${finanstilsynet.instruments.url}")
	private String finanstilsynetUrl;
	

	/*
	 * Method to convert String to LocalDate of format yyyy-MM-dd
	 * 
	 * @param String
	 * 
	 * @return LocalDate
	 */
	public LocalDate getLocalDate(String date) {
		log.debug("Date to be converted: {}", date);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FinanstilsynetConstants.DATE_FORMAT);
		var convertedDate = LocalDate.parse(date, formatter);
		log.debug("Converted Date: {}", convertedDate);
		return convertedDate;
	}

	/*
	 * Method to call external service to get all instrument details
	 * 
	 *  @return InstrumentShortingHistory[]
	 */
	public InstrumentShortingHistory[] getInstrumentDetails() {
		InstrumentShortingHistory[] response = null;
		try {
			response = restTemplate.getForObject(finanstilsynetUrl, InstrumentShortingHistory[].class);
		} catch(Exception ex) {
			throw new TechnicalException(FinanstilsynetConstants.EXTERNAL_API_MSG);
		}
		return response;
	}
}
