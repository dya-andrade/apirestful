package br.com.api.restful.unitests.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public static LocalDateTime dateFormatter(String date) {
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

		return dateTime;
	}
	
	public static String dateInString(LocalDateTime dateTime) {
		String dateStr = dateTime.format(formatter);

		return dateStr;
	}

}
