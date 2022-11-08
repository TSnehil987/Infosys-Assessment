package com.infosys.javaassessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.infosys.javaassessment.dto.AggregatedShortEvent;
import com.infosys.javaassessment.dto.InstrumentShortingHistory;
import com.infosys.javaassessment.dto.UnderlyingShortPosition;
import com.infosys.javaassessment.exception.InstrumentNotFoundException;
import com.infosys.javaassessment.exception.InvalidInputException;
import com.infosys.javaassessment.util.FinanstilsynetConstants;
import com.infosys.javaassessment.util.FinanstilsynetUtils;

@SpringBootTest
class FinanstilsynetServiceImplTest {
	
	private static final String FROM_DATE = "2022-10-10";
	private static final String TO_DATE = "2022-11-10";
	private static final String INVALID_FROM_DATE = "2022-11-10";
	private static final String INVALID_TO_DATE = "2022-10-10";
	private static final String ISIN = "BMG9156K1018";
	

	@InjectMocks
	FinanstilsynetServiceImpl finanstilsynetServiceImpl;
	
	@Mock
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
	
	private List<InstrumentShortingHistory> prepareInstrumentShortingHistoryList() {
		InstrumentShortingHistory instEvent = InstrumentShortingHistory.of(ISIN, "2020 BULKERS", prepareAggregatedShortEventList());
		List<InstrumentShortingHistory> shrtEvntList = new ArrayList<>();
		shrtEvntList.add(instEvent);
		return shrtEvntList;
	}
	
	private InstrumentShortingHistory[] prepareInstrumentShortingHistory() {
		InstrumentShortingHistory[] array = new InstrumentShortingHistory[1];
		array[0] = InstrumentShortingHistory.of(ISIN, "2020 BULKERS", prepareAggregatedShortEventList());
		return array;	
	}
	
	private void getFormattedDate(String fromDate, String toDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FinanstilsynetConstants.DATE_FORMAT);
		var convertedFromDate = LocalDate.parse(fromDate, formatter);
		var convertedToDate = LocalDate.parse(toDate, formatter);
		when(finanstilsynetUtils.getLocalDate(fromDate)).thenReturn(convertedFromDate);
		when(finanstilsynetUtils.getLocalDate(toDate)).thenReturn(convertedToDate);	
	}
	
	@Test
	void getInstrumentListTest() {
		getFormattedDate(FROM_DATE, TO_DATE);
		var instList = finanstilsynetServiceImpl.getInstrumentList(prepareInstrumentShortingHistoryList(), FROM_DATE, TO_DATE, ISIN);
		assertEquals(ISIN, instList.getIsin());
	}
	
	@Test
	void isValidDateTest() {
		getFormattedDate(FROM_DATE, TO_DATE);
		var validDate = finanstilsynetServiceImpl.isValidDate(FROM_DATE, TO_DATE);
		assertEquals(true, validDate);
	}
	
	@Test
	void isNotValidDateTest() {
		getFormattedDate(INVALID_FROM_DATE, INVALID_TO_DATE);
		InvalidInputException invalidInputException = assertThrows(InvalidInputException.class, 
				() -> finanstilsynetServiceImpl.isValidDate(INVALID_FROM_DATE, INVALID_TO_DATE));
		assertEquals(FinanstilsynetConstants.FROM_TO_DATE_VALIDATION, invalidInputException.getMessage());
	}
	
	@Test
	void getShortPositionTest() {
		getFormattedDate(FROM_DATE, TO_DATE);
		when(finanstilsynetUtils.getInstrumentDetails()).thenReturn(prepareInstrumentShortingHistory());
		var result = finanstilsynetServiceImpl.getShortPosition(ISIN, FROM_DATE, TO_DATE);
		assertEquals(ISIN, result.get(0).getIsin());
	}
	
	@Test
	void getShortPositionTest_IsinNotFound() {
		getFormattedDate(FROM_DATE, TO_DATE);
		when(finanstilsynetUtils.getInstrumentDetails()).thenReturn(prepareInstrumentShortingHistory());
		InstrumentNotFoundException instrumentNotFoundException = assertThrows(InstrumentNotFoundException.class, 
				() -> finanstilsynetServiceImpl.getShortPosition("TEST123", FROM_DATE, TO_DATE));
		assertEquals(FinanstilsynetConstants.ERROR_MSG, instrumentNotFoundException.getMessage());
	}
	
	@Test
	void getShortPositionTest_MissingFromOrToDate() {
		when(finanstilsynetUtils.getInstrumentDetails()).thenReturn(prepareInstrumentShortingHistory());
		InvalidInputException invalidInputExc = assertThrows(InvalidInputException.class, 
				() -> finanstilsynetServiceImpl.getShortPosition(ISIN, FROM_DATE, null));
		assertEquals(FinanstilsynetConstants.MISSING_DATE_VALIDATION, invalidInputExc.getMessage());
	}
	
	@Test
	void getShortPositionTest_WithoutFromAndToDate() {
		when(finanstilsynetUtils.getInstrumentDetails()).thenReturn(prepareInstrumentShortingHistory());
		var result = finanstilsynetServiceImpl.getShortPosition(ISIN, null, null);
		assertEquals(ISIN, result.get(0).getIsin());
	}
}
