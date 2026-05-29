package com.bing.utils.func;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import com.bing.utils.eums.CustomHttpHeader;
import com.bing.utils.eums.TokenType;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {
	public static final String ATC = "ATC";
	public static final String RTC = "RTC";
	
	private CookieUtils() {
		super();
	}

	public static void setCookie(HttpServletResponse response, String cookieName, String value, long maxAge) {
		ResponseCookie cookie = ResponseCookie.from(cookieName, value)
				.path("/")
				.httpOnly(true)
				.secure(true)
				.sameSite("None")
				.maxAge(maxAge)
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		return Optional.ofNullable(request.getCookies())
				.map(Arrays::stream)
				.orElse(null)
				.filter(cookie-> cookieName.equals(cookie.getName()))
				.findFirst()
				.map(Cookie::getValue)
				.orElse(null);
	}
}
