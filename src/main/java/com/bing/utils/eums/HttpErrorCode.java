package com.bing.utils.eums;

public enum HttpErrorCode {
    // 400
	DUPLICATED_DATA(409, "DUPLICATED_DATA"),

    // 400
	INVALID_REQUEST(400, "INVALID_REQUEST"),
	
    // 400
    VALIDATION_ERROR(400, "VALIDATION_ERROR"),

    // 401
    UNAUTHORIZED(401, "UNAUTHORIZED"),

    // 403
    FORBIDDEN(403, "FORBIDDEN"),

    // 404
    NOT_FOUND(404, "NOT_FOUND"),

    // 503
    SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE"),

    // 500
    INTERNAL_ERROR(500, "INTERNAL_ERROR");
	
	private final int statusCode;
	private final String message;
	
	HttpErrorCode(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}
	
	
}
