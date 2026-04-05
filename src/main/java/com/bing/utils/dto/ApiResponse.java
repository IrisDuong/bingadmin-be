package com.bing.utils.dto;

import lombok.Builder;

@Builder
public record ApiResponse<T>(T data, String message, int httpStatusCode, String timestamp) {

}
