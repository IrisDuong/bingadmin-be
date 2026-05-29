package com.bing.auth.config;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.bing.auth.dto.TokenDTO;
import com.bing.auth.service.RefreshTokenServiceImpl;
import com.bing.utils.func.CommonUtils;
import com.bing.utils.func.CookieUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthSuccessHandler authSuccessHandler;
	private final AuthExceptionHandler authExceptionHandler;
	private final TokenResolver tokenResolver;
	private final TokenAuthProvider tokenAuthProvider;

	@Value("${URL_FRONTEND}")
	String urlFrontend;

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(List.of(tokenAuthProvider));
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cors = new CorsConfiguration();
		cors.setAllowCredentials(true);
		cors.setAllowedHeaders(List.of("*"));
		cors.setAllowedMethods(List.of("GET","POST","PATCH","PUT","DELETE","OPTIONS"));
		cors.setAllowedOrigins(List.of(urlFrontend));
		cors.setMaxAge(Duration.ofMinutes(60));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cors);
		return source;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		BearerTokenAuthenticationFilter filter = new BearerTokenAuthenticationFilter(authenticationManager());
		filter.setBearerTokenResolver(tokenResolver);
		
		http.cors(cors->cors.configurationSource(corsConfigurationSource()))
		    .csrf(csrf->csrf.disable())
		    .authorizeHttpRequests(auth-> auth
		    		.requestMatchers("/login","/oauth2/**","/auth/refresh-token").permitAll()
		    		.anyRequest().authenticated()
		    )
		    .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		    .oauth2Login(oauth2Login-> oauth2Login.successHandler(authSuccessHandler))
		    .exceptionHandling(ex-> ex.authenticationEntryPoint(authExceptionHandler))
		    .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
