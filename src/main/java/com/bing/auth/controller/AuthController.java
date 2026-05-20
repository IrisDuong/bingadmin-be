package com.bing.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bing.utils.dto.ApiResponse;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.exception.BaseException;
import com.bing.utils.func.ApiUtils;
import com.bing.utils.func.CommonUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
}
