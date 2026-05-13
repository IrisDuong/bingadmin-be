package com.bing.setting.basedata.service;

import java.util.List;

import com.bing.setting.basedata.dto.RefCodeGroupDTO;
import com.bing.setting.basedata.entity.RefCodeGroup;
import com.bing.utils.dto.PaginationResponseDTO;
import com.bing.utils.eums.ActiveStatus;
import com.bing.utils.eums.CodeType;
import com.bing.utils.eums.UsageStatus;
import com.bing.utils.service.IDataConverter;

public interface RefCodeGroupService extends IDataConverter<RefCodeGroup, RefCodeGroupDTO.Response, RefCodeGroupDTO.Request>{

	void create(RefCodeGroupDTO.Request request);
	RefCodeGroupDTO.Response getRefCodeGroupDetail(String featCodeNo, String codeGroupNo);
	void deleteRefCodeGroup(String featCodeNo, String codeGroupNo);
	PaginationResponseDTO<RefCodeGroupDTO.Response> search(RefCodeGroupDTO.Request request);
	RefCodeGroupDTO.Response update(RefCodeGroupDTO.Request request);
	
	@Override
	default RefCodeGroupDTO.Response convertToResponseDto(RefCodeGroup entity) {
		return RefCodeGroupDTO.Response.builder()
				.codeGroupNo(entity.getCodeGroupNo())
				.featCodeNo(entity.getFeatCodeNo())
				.codeType(entity.getCodeType())
				.activeStatus(entity.getActiveStatus())
				.usageStatus(entity.getUsageStatus())
				.description(entity.getDescription())
				.build();
				
	}
	@Override
	default RefCodeGroup convertToEntity(RefCodeGroupDTO.Request request) {
		return  RefCodeGroup.builder()
				.codeGroupNo(request.getCodeGroupNo())
				.activeStatus(ActiveStatus.getByValue(request.getActiveStatusValue()))
				.usageStatus(UsageStatus.getByValue(request.getUsageStatusValue()))
				.codeType(CodeType.getByValue(request.getCodeTypeValue()))
				.featCodeNo(request.getFeatCodeNo())
				.description(request.getDescription())
				.build();
	}
	
	static void test() {
		System.out.println("test static service");
	}
	
}
