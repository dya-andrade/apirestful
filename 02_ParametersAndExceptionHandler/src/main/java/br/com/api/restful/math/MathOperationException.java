package br.com.api.restful.math;

import br.com.api.restful.converters.NumberConverter;
import br.com.api.restful.exceptions.UnsupportMathOperationException;

public abstract class MathOperationException {
		
	public static void exceptionOne(String number) {
		if (number == null)
			throw new UnsupportMathOperationException("Please enter a numeric value!");
		
		if (NumberConverter.isNumeric(number)) 
			throw new UnsupportMathOperationException("Please set a numeric value!");
	}
	
	public static void exceptionTwo(String numberOne, String numberTwo) {
		if (numberOne == null || numberTwo == null)
			throw new UnsupportMathOperationException("Please enter a numeric value!");
		
		if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) 
			throw new UnsupportMathOperationException("Please set a numeric value!");
	}
}
