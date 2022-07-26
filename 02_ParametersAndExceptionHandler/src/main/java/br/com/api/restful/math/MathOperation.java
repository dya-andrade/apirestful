package br.com.api.restful.math;

import br.com.api.restful.converters.NumberConverter;

public class MathOperation {

	public static Double squareRoot(String number) {
		MathOperationException.exceptionOne(number);
		return Math.sqrt(NumberConverter.convertToDouble(number));
	}
	
	public static Double division(String numberOne, String numberTwo) {
		MathOperationException.exceptionTwo(numberOne, numberTwo);
		return NumberConverter.convertToDouble(numberOne) / NumberConverter.convertToDouble(numberTwo);
	}
	
	public static Double mean(String numberOne, String numberTwo) {
		MathOperationException.exceptionTwo(numberOne, numberTwo);
		return (NumberConverter.convertToDouble(numberOne) + NumberConverter.convertToDouble(numberTwo)) / 2;
	}
	
	public static Double multiplication(String numberOne, String numberTwo) {
		MathOperationException.exceptionTwo(numberOne, numberTwo);
		return NumberConverter.convertToDouble(numberOne) * NumberConverter.convertToDouble(numberTwo);
	}
	
	public static Double subtraction(String numberOne, String numberTwo) {
		MathOperationException.exceptionTwo(numberOne, numberTwo);
		return NumberConverter.convertToDouble(numberOne) - NumberConverter.convertToDouble(numberTwo);
	}
	
	public static Double sum(String numberOne, String numberTwo) {
		MathOperationException.exceptionTwo(numberOne, numberTwo);
		return NumberConverter.convertToDouble(numberOne) + NumberConverter.convertToDouble(numberTwo);
	}
}
