package com.bing.auth.config;

import java.util.Arrays;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bing.utils.eums.AppHeader;
import com.bing.utils.func.CommonUtils;
import com.bing.utils.func.TokenUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenResolver implements BearerTokenResolver{

	@Override
	public String resolve(HttpServletRequest request) {
		String authHeader = request.getHeader(AppHeader.AUTHORIZATION.getHeaderName());
		if(Boolean.FALSE.equals(CommonUtils.isEmptyData(authHeader)) && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}else {
			return Arrays.stream(request.getCookies())
					.filter(cookie-> TokenUtils.ATC.equals(cookie.getName()))
					.map(Cookie::getValue)
					.filter(StringUtils::hasText)
					.findFirst()
					.orElse(null);
		}
	}

}
