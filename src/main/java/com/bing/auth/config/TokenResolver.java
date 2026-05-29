package com.bing.auth.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import com.bing.utils.eums.CustomHttpHeader;
import com.bing.utils.func.CommonUtils;
import com.bing.utils.func.CookieUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenResolver implements BearerTokenResolver{

	@Override
	public String resolve(HttpServletRequest request) {
		String authHeader = request.getHeader(CustomHttpHeader.AUTHORIZATION.getHeaderName());
		if(Boolean.FALSE.equals(CommonUtils.isEmptyData(authHeader)) && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}else {
			return CookieUtils.getCookieValue(request, CookieUtils.ATC);
		}
	}

}
