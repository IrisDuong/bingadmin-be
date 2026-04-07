package com.bing.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bing.utils.dto.ErrorResponse;
import com.bing.utils.func.DateUtils;

@RestControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex){
		ErrorResponse errorResponse = ErrorResponse.builder()
				.httpStatusCode(ex.getHttpErrorCode().getStatusCode())
				.message(ex.getMessage())
				.timestamp(DateUtils.nowWithDateTime())
				.build();
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.valueOf(errorResponse.httpStatusCode()));
	}
}
