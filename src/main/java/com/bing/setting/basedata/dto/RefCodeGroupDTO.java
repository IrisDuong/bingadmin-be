package com.bing.setting.basedata.dto;

import java.util.List;

import com.bing.utils.dto.PaginationRequestDTO;
import com.bing.utils.eums.ActiveStatus;
import com.bing.utils.eums.CodeType;
import com.bing.utils.eums.UsageStatus;

import lombok.Builder;
import lombok.Data;

public class RefCodeGroupDTO {

	@Data
	public static class Request{
		private String codeGroupNo;
		private String codeGroupName;
		private String featCodeNo;
		private Integer codeTypeValue;
		private Integer activeStatusValue;
		private Boolean usageStatusValue;
		private String description;
		private List<CodeTranslationDTO> codeTranslationDTOs;
		private PaginationRequestDTO pagination;
	}
	
	@Data
	@Builder
	public static class Response{
		private String codeGroupNo;
		private String featCodeNo;
		private CodeType codeType;
		private ActiveStatus activeStatus;
		private UsageStatus usageStatus;
		private String description;
		private List<CodeTranslationDTO> codeTranslationDTOs;
	}
}
