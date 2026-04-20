package com.bing.utils.service;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bing.utils.func.CommonUtils;

public class AuditingImpl implements AuditorAware<String>{

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(Boolean.FALSE.equals(CommonUtils.isEmptyData(authentication))
				&& Boolean.FALSE.equals(CommonUtils.isEmptyData(authentication.getPrincipal()))
		) {
			return Optional.of((String) authentication.getPrincipal());
		}
		return Optional.of("Anonymous");
	}

}
