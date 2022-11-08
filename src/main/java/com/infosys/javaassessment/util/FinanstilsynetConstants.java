package com.infosys.javaassessment.util;

public class FinanstilsynetConstants {
	
	private FinanstilsynetConstants() {
	    throw new IllegalStateException("Utility class");
	  }

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public static final String ERROR_MSG = "No record found with given ISIN";
	
	public static final String FROM_TO_DATE_VALIDATION = "FromDate cannot be greater than ToDate. Please make correction & try again.";

	public static final String MISSING_DATE_VALIDATION = "FromDate or ToDate is missing. Please check and try again.";

	public static final String EXTERNAL_API_MSG = "No response from external api";
	
	public static final String INVALID_ISIN_MSG = "ISIN is not valid. Please check and try again.";
}
