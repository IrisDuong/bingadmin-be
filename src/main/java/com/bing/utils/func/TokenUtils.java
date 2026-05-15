package com.bing.utils.func;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;

import com.bing.utils.eums.CustomHttpHeader;
import com.bing.utils.eums.TokenType;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtils {
	public static final String ATC = "ATC";
	public static final String RTC = "RTC";
	
	private TokenUtils() {
		super();
	}

	public static void setCookie(HttpServletResponse response, String cookieName, String value, int maxAge) {
		String cookie = """
				%s=%s; 
				Path=/; 
				Max-Age=3600; 
				HttpOnly; 
				Secure; 
				SameSite=None
				""".format(cookieName, value);
		log.info(String.format("[AUTHENTICATION] :: cookie  = %s", cookie));
		response.setHeader(HttpHeaders.SET_COOKIE, cookie);
	}
}
