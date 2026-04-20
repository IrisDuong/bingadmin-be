package com.bing.utils.service;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bing.utils.eums.CustomHttpHeader;
import com.bing.utils.eums.Language;
import com.bing.utils.func.CommonUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LangFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String langHeader = request.getHeader(CustomHttpHeader.ACCEPT_LANG.getHeaderName());
			if(Boolean.TRUE.equals(CommonUtils.isEmptyData(langHeader)) || Boolean.TRUE.equals(CommonUtils.isEmptyData(Language.getByCode(langHeader)))) {
				LangContext.set(Language.VI.getLangCode());
			}
			
			LangContext.set(langHeader);
			filterChain.doFilter(request, response);
		} finally {
			LangContext.clear();
		}
		
	}
}
