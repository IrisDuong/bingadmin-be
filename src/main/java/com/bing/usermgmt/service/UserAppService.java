package com.bing.usermgmt.service;

import com.bing.usermgmt.dto.UserAppDTO;
import com.bing.usermgmt.dto.UserAppDTO.Request;
import com.bing.usermgmt.dto.UserAppDTO.Response;
import com.bing.usermgmt.entity.UserApp;
import com.bing.utils.service.IDataConverter;

public interface UserAppService extends IDataConverter<UserApp, UserAppDTO.Response, UserAppDTO.Request>{

	void createUser(UserAppDTO.Request request);
	Response finduserByEmail(String email);
	
	@Override
	default UserApp convertToEntity(Request request) {
		return UserApp.builder()
				.email(request.getEmail())
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.avavatar(request.getAvavatar())
				.build();
	}
	
	@Override
	default Response convertToResponseDto(UserApp entity) {
		return Response.builder()
				.email(entity.getEmail())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.avavatar(entity.getAvavatar())
				.build();
	}
}
