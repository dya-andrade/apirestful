package br.com.api.restful.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportMathOperationException extends RuntimeException {
	
	public UnsupportMathOperationException(String ex) {
		super(ex);
	}

	private static final long serialVersionUID = 1L;

}
