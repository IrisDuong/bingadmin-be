package com.bing.auth.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bing.auth.config.JwtToken;
import com.bing.auth.dto.TokenDTO;
import com.bing.redis.config.RedisService;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.eums.TokenType;
import com.bing.utils.exception.BaseException;
import com.bing.utils.func.CommonUtils;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl {
	
	/*
	 *  Dependency
	 */
	private final JwtToken jwtToken;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;
	
	/*
	 *  Constant
	 */
	private final String VALID_RT_KEY_PREFIX = "valid_refresh_token:%s:%s";
	private final String BLACKLIST_RT_KEY_PREFIX = "blacklist_refresh_token:%s:%s";
	private final long REFRESH_TOKEN_GRACE_PERIOD  = 20000;
	
	public String generateRefreshToken(TokenDTO tokenDto) {
		log.info(String.format("[GENERATE REFRESH TOKEN] :: remaining expiry = %s", Instant.now().plus(tokenDto.getRemainingRefreshTokenExpiry(), ChronoUnit.MILLIS).toString()));
		String refreshToken = UUID.randomUUID().toString();
		String hashedRefreshToken = passwordEncoder.encode(refreshToken);
		cachingValidRefreshToken(tokenDto, hashedRefreshToken);
		return hashedRefreshToken;
	}
	
	public String rotateToken(TokenDTO tokenDto,String incommingRefreshToken) {
		try {
			String existedValidRefreshToken = (String) getCachingRefreshToken(VALID_RT_KEY_PREFIX,tokenDto);

			boolean isError = false;
			StringBuilder message = new StringBuilder("");
			if(Boolean.FALSE.equals(CommonUtils.isEmptyData(existedValidRefreshToken))){
				String blacklistKey = String.format(BLACKLIST_RT_KEY_PREFIX, tokenDto.getEmail(),tokenDto.getDeviceId());
				if(Boolean.FALSE.equals(existedValidRefreshToken.equals(incommingRefreshToken))) {
					isError = true;
					if(redisService.isMemberOfKey(blacklistKey, incommingRefreshToken)) {
						message.append("Token is used. Please login again");
					}
					
					if(redisService.sizeOfKey(blacklistKey) > 0 && Boolean.FALSE.equals(redisService.isMemberOfKey(blacklistKey, incommingRefreshToken))) {
						message.append("The token was counterfeited");
					}
					revokeValidRefreshToken(tokenDto);
				}
			}else {
				isError = true;
			}
			
			if(isError) {
				throw new BaseException(HttpErrorCode.UNAUTHORIZED,message.toString());
			}
			
			//reset remaining expiry of token in both blacklist and valid
			updateRemainingRefreshTokenExpiry(tokenDto);
			log.info(String.format("[ROTATE REFRESH TOKEN] :: remaining expiry = %s", Instant.now().plus(tokenDto.getRemainingRefreshTokenExpiry(), ChronoUnit.MILLIS).toString()));
			
			// add old token to blacklist
			cachingUsedRefreshToken(tokenDto, incommingRefreshToken);
			
			// store new valid token
			String newRefreshToken = generateRefreshToken(tokenDto);
			return newRefreshToken;
		} catch (Exception e) {
			throw new BaseException(HttpErrorCode.UNAUTHORIZED, e.getMessage());
		}
	}
	
	public void revokeValidRefreshToken(TokenDTO tokenDto) {
		String key = String.format(VALID_RT_KEY_PREFIX, tokenDto.getEmail(),tokenDto.getDeviceId());
		redisService.delete(key);
	}

	public void cachingValidRefreshToken(TokenDTO tokenDto,Object validToken) {
		String key = String.format(VALID_RT_KEY_PREFIX, tokenDto.getEmail(),tokenDto.getDeviceId());
		redisService.set(key, validToken, tokenDto.getRemainingRefreshTokenExpiry());
	}

	public void cachingUsedRefreshToken(TokenDTO tokenDto,Object usedToken) {
		String key = String.format(BLACKLIST_RT_KEY_PREFIX, tokenDto.getEmail(),tokenDto.getDeviceId());
		redisService.set(key, new Object[] {usedToken}, tokenDto.getRemainingRefreshTokenExpiry());
	}
	
	public Object getCachingRefreshToken(String prefix,TokenDTO tokenDto) {
		String key = String.format(prefix, tokenDto.getEmail(),tokenDto.getDeviceId());
		return redisService.get(key);
	}
	
	private void updateRemainingRefreshTokenExpiry(TokenDTO tokenDto) {
		String key = String.format(VALID_RT_KEY_PREFIX, tokenDto.getEmail(),tokenDto.getDeviceId());
		long newRemainingExpiry =  redisService.getExpire(key);
		if(newRemainingExpiry > 0) {
			tokenDto.setRemainingRefreshTokenExpiry(newRemainingExpiry);
		}else {
			throw new BaseException(HttpErrorCode.INTERNAL_ERROR, "Session working is expired");
		}
		
	}
}
