package com.bing.utils.func;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;

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
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
}
