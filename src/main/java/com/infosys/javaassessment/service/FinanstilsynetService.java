package com.infosys.javaassessment.service;

import java.util.List;

import com.infosys.javaassessment.dto.InstrumentShortingHistory;

public interface FinanstilsynetService {

	public List<InstrumentShortingHistory> getShortPosition(String isin, String fromDate, String toDate);
}
