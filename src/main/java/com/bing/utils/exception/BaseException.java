package com.bing.utils.exception;

import com.bing.utils.eums.HttpErrorCode;

public class BaseException extends RuntimeException{

	private final HttpErrorCode httpErrorCode;

	public  BaseException(HttpErrorCode httpErrorCode,String message) {
		super(message);
		this.httpErrorCode = httpErrorCode;
	}

	public  BaseException(HttpErrorCode httpErrorCode) {
		super(httpErrorCode.getMessage());
		this.httpErrorCode = httpErrorCode;
	}

	public HttpErrorCode getHttpErrorCode() {
		return httpErrorCode;
	}
	
}
