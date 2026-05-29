package com.bing.auth.dto;

import java.time.Instant;
import java.util.Objects;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenDTO{
	private String email;
	private String deviceId;
	private String deviceName;
	private long remainingRefreshTokenExpiry;
	
	@Data
	@Builder
	public static class Response {
		private String accessToken;
		private String refreshToken;
	}
}
