package com.bing.usermgmt.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bing.usermgmt.dto.UserAppDTO.Request;
import com.bing.usermgmt.dto.UserAppDTO.Response;
import com.bing.usermgmt.entity.UserApp;
import com.bing.usermgmt.repo.UserAppRepository;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.exception.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAppServiceImpl implements UserAppService{
	private final UserAppRepository userAppRepository;
	
	@Override
	public void createUser(Request request) {
		userAppRepository.save(this.convertToEntity(request));
	}

	@Override
	public Response finduserByEmail(String email) {
		UserApp result = userAppRepository.findByEmail(email)
				.orElseThrow(()-> new BaseException(HttpErrorCode.NOT_FOUND));
		return this.convertToResponseDto(result);
	}

}
