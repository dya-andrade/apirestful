package br.com.api.restful.converters;

public class NumberConverter {
	
	public static Double convertToDouble(String strNumber) {
		String number = replaceStrNumber(strNumber);

		return Double.parseDouble(number);
	}
	
	public static boolean isNumeric(String strNumber) {
		String number = replaceStrNumber(strNumber);

		return number.matches("[-+]?[0-9]*\\.?[0-9]+");
	}
	
	private static String replaceStrNumber(String number) {
		return number.replaceAll(",", ".");
	}
}
