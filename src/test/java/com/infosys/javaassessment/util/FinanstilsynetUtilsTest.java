package com.infosys.javaassessment.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.infosys.javaassessment.dto.AggregatedShortEvent;
import com.infosys.javaassessment.dto.InstrumentShortingHistory;
import com.infosys.javaassessment.dto.UnderlyingShortPosition;
import com.infosys.javaassessment.exception.TechnicalException;

@SpringBootTest
class FinanstilsynetUtilsTest {

	private static final String STRING_DATE = "2022-10-10";
	private static final String ISIN = "BMG9156K1018";
	private static final String BASE_URL = "https://ssr.finanstilsynet.no/api/v2/instruments/";
	
	@InjectMocks
	FinanstilsynetUtils finanstilsynetUtils;
	
	private List<UnderlyingShortPosition> prepareUnderlyingShortPositionList() {
		UnderlyingShortPosition shortPos = UnderlyingShortPosition.of("2022-11-02", 1.29f, 288035L, "WorldQuant LLC");
		List<UnderlyingShortPosition> shrtPosList = new ArrayList<>();
		shrtPosList.add(shortPos);
		return shrtPosList;
	}
	
	private List<AggregatedShortEvent> prepareAggregatedShortEventList() {
		AggregatedShortEvent shortEvnt1 = AggregatedShortEvent.of("2022-10-28", 2.49f, 555855L, prepareUnderlyingShortPositionList());
		AggregatedShortEvent shortEvnt2 = AggregatedShortEvent.of("2022-11-02", 2.58f, 574819L, prepareUnderlyingShortPositionList());
		List<AggregatedShortEvent> shrtEvntList = new ArrayList<>();
		shrtEvntList.add(shortEvnt1);
		shrtEvntList.add(shortEvnt2);
		return shrtEvntList;
	}
	
	private InstrumentShortingHistory[] prepareInstrumentShortingHistory() {
		InstrumentShortingHistory[] array = new InstrumentShortingHistory[1];
		array[0] = InstrumentShortingHistory.of(ISIN, "2020 BULKERS", prepareAggregatedShortEventList());
		return array;	
	}
	
	@Test
	void getLocalDateTest() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FinanstilsynetConstants.DATE_FORMAT);
		var expectedDate = LocalDate.parse(STRING_DATE, formatter);
		var result = finanstilsynetUtils.getLocalDate(STRING_DATE);
		assertEquals(expectedDate, result);
	}
	
	@Test
	void getInstrumentDetailsTest_ThrowsException() {
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		when(restTemplate.getForObject(BASE_URL,
				InstrumentShortingHistory[].class)).thenReturn(prepareInstrumentShortingHistory());
		TechnicalException technicalException = assertThrows(TechnicalException.class, 
				() -> finanstilsynetUtils.getInstrumentDetails());
		assertEquals(FinanstilsynetConstants.EXTERNAL_API_MSG, technicalException.getMessage());
	}
	
	@Test
	void getInstrumentDetailsTest() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
	    URI uri = new URI(BASE_URL);
	    ResponseEntity<InstrumentShortingHistory[]> result = restTemplate.getForEntity(uri, 
	    		InstrumentShortingHistory[].class);
	     
	    assertEquals(200, result.getStatusCodeValue());
	}
}
