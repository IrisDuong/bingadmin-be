package com.bing.auth.config;

import java.util.List;


import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.eums.TokenType;
import com.bing.utils.exception.BaseException;
import com.bing.utils.func.CommonUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenAuthProvider implements AuthenticationProvider{
	private final JwtToken jwtToken;

	@Override
	public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String token = (String) authentication.getCredentials();
		if(Boolean.FALSE.equals(CommonUtils.isEmptyData(token)) && jwtToken.validateToken(token, TokenType.ACCESS_TOKEN)) {
			String sub = jwtToken.getClaim(token, Claims::getSubject);
			return new UsernamePasswordAuthenticationToken(sub, null,List.of(new SimpleGrantedAuthority("ROLE_USER")));
		}
		return null;
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
