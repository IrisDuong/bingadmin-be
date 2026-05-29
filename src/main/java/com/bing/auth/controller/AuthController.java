package com.bing.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bing.auth.config.JwtToken;
import com.bing.auth.dto.TokenDTO;
import com.bing.auth.service.RefreshTokenServiceImpl;
import com.bing.usermgmt.dto.UserAppDTO;
import com.bing.usermgmt.service.UserAppService;
import com.bing.utils.dto.ApiResponse;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.eums.TokenType;
import com.bing.utils.exception.BaseException;
import com.bing.utils.func.ApiUtils;
import com.bing.utils.func.CommonUtils;
import com.bing.utils.func.CookieUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final RefreshTokenServiceImpl tokenServiceImpl;
	private final JwtToken jwtToken;
	private final UserAppService userAppService;

	@Value("${app.sec.jwt.rt.exp}")
	long refreshTokenExp;

	@Value("${app.sec.jwt.at.exp}")
	long accessTokenExp;
	
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<AuthenticatedUser>> getAuthenticatedUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
		if (Boolean.FALSE.equals(CommonUtils.isEmptyData(oAuth2User))) {
			String name = oAuth2User.getAttribute("name");
			String email = oAuth2User.getAttribute("email");
			String picture = oAuth2User.getAttribute("picture");
			AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
					.name(name)
					.email(email)
					.phoneNo("")
					.picture(picture)
					.build();
			return ApiUtils.buildApiResponse(authenticatedUser, HttpStatus.OK, "Logged User found");
	    }else {
			throw new BaseException(HttpErrorCode.UNAUTHORIZED);
	    }
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse<TokenDTO.Response>> refreshToken(
			@CookieValue(name = "RTC", required = false) String incommingRefreshToken,
			@RequestHeader(name = "X-User-Id", required = false) String email,
			HttpServletResponse response
	){
		if(Optional.ofNullable(incommingRefreshToken).isPresent()) {

				// rotate refresh token
				TokenDTO tokenDTO = TokenDTO.builder()
						.email(email)
						.deviceId("hp01")
						.build();
				String newRefreshToken = tokenServiceImpl.rotateToken(tokenDTO, incommingRefreshToken);
				


				// create new token
				UserAppDTO.Response userAppResponse = userAppService.finduserByEmail(email);
				
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("email", email);
				attributes.put("name", userAppResponse.getFirstName());
				attributes.put("picture", userAppResponse.getAvavatar());
				String newAccessToken = jwtToken.generateAccessToken(attributes);

				// set to cookie
				CookieUtils.setCookie(response, CookieUtils.RTC, newRefreshToken, refreshTokenExp);
				CookieUtils.setCookie(response, CookieUtils.ATC, newAccessToken, accessTokenExp);
				
				TokenDTO.Response tokenResponse = TokenDTO.Response.builder()
						.accessToken(newAccessToken)
						.refreshToken(newRefreshToken)
						.build();
				
				return ApiUtils.buildApiResponse(tokenResponse, HttpStatus.OK, "Refresh token successfully");
		}
		throw new BaseException(HttpErrorCode.INVALID_REQUEST);
	}
}
