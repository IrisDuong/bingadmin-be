package com.bing.auth.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bing.utils.eums.TokenType;
import com.bing.utils.func.TokenUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	private final JwtToken jwtToken;
	
	@Value("${URL_FRONTEND}")
	String urlFrontend;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		log.info(String.format("[AUTHENTICATION] :: oAuth2User.getName()  = %s", oAuth2User.getName()));
		String email = oAuth2User.getAttribute("email");
		String accessToken = jwtToken.createToken(email, TokenType.ACCESS_TOKEN);
		String refreshToken = jwtToken.createToken(email, TokenType.REFRESH_TOKEN);
		TokenUtils.setCookie(response, TokenUtils.ATC, accessToken, 3600);
		TokenUtils.setCookie(response, TokenUtils.RTC, refreshToken, 7200);
		log.info(String.format("[AUTHENTICATION] :: access token  = %s", accessToken));
		
		response.sendRedirect(urlFrontend);
	}
}
