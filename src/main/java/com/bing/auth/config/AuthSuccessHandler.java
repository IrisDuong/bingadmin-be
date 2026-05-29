package com.bing.auth.config;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bing.auth.dto.TokenDTO;
import com.bing.auth.service.RefreshTokenServiceImpl;
import com.bing.usermgmt.dto.UserAppDTO;
import com.bing.usermgmt.service.UserAppService;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.eums.TokenType;
import com.bing.utils.exception.BaseException;
import com.bing.utils.func.CookieUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
	private final RefreshTokenServiceImpl tokenServiceImpl;
	private final UserAppService userAppService;
	
	@Value("${URL_FRONTEND}")
	String urlFrontend;
	
	@Value("${app.sec.jwt.rt.exp}")
	long refreshTokenExp;

	@Value("${app.sec.jwt.at.exp}")
	long accessTokenExp;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		try {
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
			log.info(String.format("[AUTHENTICATION] :: oAuth2User.getName()  = %s", oAuth2User.getName()));

			// create user app
			UserAppDTO.Request userAppRequest = UserAppDTO.Request.builder()
					.email(oAuth2User.getAttribute("email"))
					.firstName(oAuth2User.getAttribute("family_name"))
					.lastName(oAuth2User.getAttribute("given_name"))
					.avavatar(oAuth2User.getAttribute("picture"))
					.build();
			userAppService.createUser(userAppRequest);
			
			String accessToken = jwtToken.generateAccessToken(oAuth2User);

			// generate refresh token and also store in Redis
			TokenDTO tokenDto = TokenDTO.builder()
					.deviceId("hp01")
					.deviceName("LAPTOP HP RNKP05")
					.email(oAuth2User.getAttribute("email"))
					.remainingRefreshTokenExpiry(refreshTokenExp)
					.build();
			String refreshToken = tokenServiceImpl.generateRefreshToken(tokenDto);
			
			// set token to cookie and respond to client
			CookieUtils.setCookie(response, CookieUtils.RTC, refreshToken, refreshTokenExp);
			CookieUtils.setCookie(response, CookieUtils.ATC, accessToken, accessTokenExp);
			response.sendRedirect(urlFrontend);
			
		} catch (NullPointerException e) {
			throw new BaseException(HttpErrorCode.UNAUTHORIZED);
		}
	}
}
