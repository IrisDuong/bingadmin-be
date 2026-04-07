package com.bing.utils.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(int httpStatusCode, String message, String timestamp) {

}
