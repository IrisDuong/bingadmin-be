package com.bing.usermgmt.dto;

import lombok.Builder;
import lombok.Data;

public class UserAppDTO {

	@Data
	@Builder
	public static class Request{
		private String email;
		private String firstName;
		private String lastName;
		private String avavatar;
	}
	
	@Data
	@Builder
	public static class Response{
		private String email;
		private String firstName;
		private String lastName;
		private String avavatar;
	}
}
