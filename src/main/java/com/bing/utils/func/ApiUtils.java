package com.bing.utils.func;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bing.utils.dto.ApiResponse;

public class ApiUtils <T>{

	private ApiUtils() {
		super();
	}

	public <T> ResponseEntity<ApiResponse<T>> buildApiResponse(T data, HttpStatus httpStatus, String message){
		ApiResponse<T> response = ApiResponse.<T>builder()
				.data(data)
				.httpStatusCode(httpStatus.value())
				.message(message)
				.timestamp(DateUtils.nowWithDateTime())
				.build();
		return new ResponseEntity<ApiResponse<T>>(response, httpStatus);
	}
}
