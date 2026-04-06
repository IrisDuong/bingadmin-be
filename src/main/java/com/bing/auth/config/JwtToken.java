package com.bing.auth.config;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bing.utils.eums.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.SignatureException;

@Component
@Slf4j
public class JwtToken {

	
	@Value("${app.sec.jwt.at.secret}")
	String accessTokenSecretKey;
	
	@Value("${app.sec.jwt.rt.secret}")
	String refreshTokenSecretKey;

	
	@Value("${app.sec.jwt.at.exp}")
	long accessTokenExp;
	
	@Value("${app.sec.jwt.rt.exp}")
	long refreshTokenExp;
	
	private Key keys(TokenType tokenType) {
		return switch(tokenType) {
		case ACCESS_TOKEN -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecretKey));
		case REFRESH_TOKEN -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecretKey));
		default -> throw new IllegalArgumentException("Token type is invalid");
		};
	}
	
	public String createToken(String sub, TokenType tokenType) {
		long exp = tokenType == TokenType.ACCESS_TOKEN ? accessTokenExp : refreshTokenExp;
		return Jwts.builder()
				.setSubject(sub)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + exp))
				.signWith(keys(tokenType))
				.compact();
	}
	
	public <T> T getClaim(String token, Function<Claims, T> claimsResover) {
		final Claims claims = extractAllClaims(token);
		return claimsResover.apply(claims);
	}
	public Claims extractAllClaims(String token) {
		return 	Jwts.parserBuilder()
				.setSigningKey(keys(TokenType.ACCESS_TOKEN))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public boolean validateToken(String token, TokenType tokenType) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(keys(tokenType))
			.build()
			.parseClaimsJws(token);
			return true;
		 } catch (SignatureException e) {
	            // Chữ ký không hợp lệ
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
        } catch (MalformedJwtException e) {
            // Token sai định dạng
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
        } catch (ExpiredJwtException e) {
            // Token hết hạn
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
//			 throw e;
        } catch (UnsupportedJwtException e) {
            // Token không hỗ trợ
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
        } catch (IllegalArgumentException e) {
            // Claims string trống
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
        }
		return false;
	}
}
