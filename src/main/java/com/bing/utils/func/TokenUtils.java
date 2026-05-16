package com.bing.utils.func;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import com.bing.utils.eums.CustomHttpHeader;
import com.bing.utils.eums.TokenType;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenUtils {
	public static final String ATC = "ATC";
	public static final String RTC = "RTC";
	
	private TokenUtils() {
		super();
	}

	public static void setCookie(HttpServletResponse response, String cookieName, String value, int maxAge) {
		ResponseCookie cookie = ResponseCookie.from(cookieName, value)
				.path("/")
				.httpOnly(true)
				.secure(true)
				.sameSite("None")
				.maxAge(maxAge)
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
