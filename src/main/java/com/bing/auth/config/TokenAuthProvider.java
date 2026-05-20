package com.bing.auth.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
			Claims claims = jwtToken.extractAllClaims(token);
			String email = claims.getSubject();
			String name = claims.get("name",String.class);
			String picture = claims.get("picture", String.class);
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("email", email);
			attributes.put("name", name);
			attributes.put("picture", picture);
			OAuth2User oAuth2User = new DefaultOAuth2User(authorities, attributes, "email");
			return new UsernamePasswordAuthenticationToken(oAuth2User, token, authorities);
		}
		return null;
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
