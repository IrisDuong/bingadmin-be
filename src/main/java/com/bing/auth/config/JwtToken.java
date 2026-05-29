package com.bing.auth.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
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
	
	private Key keys() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecretKey));
	}
	
	public String generateAccessToken(Map<String, Object> attr) {
		Claims claims = Jwts.claims().setSubject((String)attr.get("email"));
		claims.put("name", attr.get("name"));
		claims.put("picture", attr.get("picture"));
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + accessTokenExp))
				.signWith(keys())
				.compact();
	}
	
	public String generateAccessToken(OAuth2User oAuth2User) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("name", oAuth2User.getAttribute("name"));
		attributes.put("picture", oAuth2User.getAttribute("picture"));
		attributes.put("email", oAuth2User.getAttribute("email"));
		return generateAccessToken(attributes);
		
	}
	public <T> T getClaim(String token, Function<Claims, T> claimsResover) {
		final Claims claims = extractAllClaims(token);
		return claimsResover.apply(claims);
	}
	
	public Claims extractAllClaims(String token) {
		return 	Jwts.parserBuilder()
				.setSigningKey(keys())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(keys())
			.build()
			.parseClaimsJws(token);
			return true;
		 } catch (SignatureException e) {
	            // Chữ ký không hợp lệ
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
			 throw e;
        } catch (MalformedJwtException e) {
            // Token sai định dạng
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
			 throw e;
        } catch (ExpiredJwtException e) {
            // Token hết hạn
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
			 throw e;
        } catch (UnsupportedJwtException e) {
            // Token không hỗ trợ
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
			 throw e;
        } catch (IllegalArgumentException e) {
            // Claims string trống
			 log.error(String.format("[JWT-TOKEN-ERROR] :: %s", e.getMessage()));
			 throw e;
        }
	}
}
