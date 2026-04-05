package com.bing.utils.func;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	public static final String ISO_DATE = "yyyy-MM-dd";
	public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String ISO_COMPACT = "yyyyMMdd";
	public static final String VN_DATE = "dd/MM/yyyy";
	public static final String VN_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
	public static final String VN_COMPACT = "ddMMyyyy";
	
	private DateUtils() {
		super();
	}

	public static DateTimeFormatter getFormatter(String pattern) {
		try {
			return DateTimeFormatter.ofPattern(pattern);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Date pattern is invalid", e);
		}
	}
	
	public static String formatDateTime(LocalDateTime ldt, String pattern) {
			return ldt.format(getFormatter(pattern));
	}
	
	public static String formatDate(LocalDate ld, String pattern) {
		return ld.format(getFormatter(pattern));
	}
	
	public static String nowWithDate() {
		return formatDate(LocalDate.now(), ISO_COMPACT);
	}
	
	public static String nowWithDateTime() {
		return formatDateTime(LocalDateTime.now(), ISO_COMPACT);
	}
	
	public static String formatDateCompactISO(String ld) {
		LocalDate verifyParser = LocalDate.parse(ld, getFormatter(ISO_DATE));
		return verifyParser.format(getFormatter(ISO_COMPACT));
	}
	
	public static String formatDateCompactVN(String ld) {
		LocalDate verifyParser = LocalDate.parse(ld, getFormatter(VN_DATE));
		return verifyParser.format(getFormatter(VN_COMPACT));
	}
}
