package com.bing.setting.basedata.service;

import java.util.List;

import com.bing.setting.basedata.dto.RefCodeItemDTO;
import com.bing.setting.basedata.dto.RefCodeItemDTO.Request;
import com.bing.setting.basedata.dto.RefCodeItemDTO.Response;
import com.bing.setting.basedata.entity.RefCodeItem;
import com.bing.setting.basedata.entity.RefCodeItemPK;
import com.bing.utils.eums.ActiveStatus;
import com.bing.utils.eums.CodeType;
import com.bing.utils.eums.UsageStatus;
import com.bing.utils.service.IDataConverter;

public interface RefCodeItemService extends IDataConverter<RefCodeItem, RefCodeItemDTO.Response, RefCodeItemDTO.Request>{

	void createByList(List<RefCodeItemDTO.Request> requests);
	
	@Override
	default RefCodeItem convertToEntity(Request request) {
		RefCodeItemPK id = RefCodeItemPK.builder()
				.codeGroupNo(request.getCodeGroupNo())
				.codeItemNo(request.getCodeItemNo())
				.build();
		return RefCodeItem.builder()
				.id(id)
				.activeStatus(ActiveStatus.getByValue(request.getActiveStatusValue()))
				.usageStatus(UsageStatus.getByValue(request.getUsageStatusValue()))
				.codeType(CodeType.getByValue(request.getCodeTypeValue()))
				.featCodeNo(request.getFeatCodeNo())
				.description(request.getDescription())
				.build();
	}
	
	@Override
	default Response convertToResponseDto(RefCodeItem entity) {
		return RefCodeItemDTO.Response.builder()
			.featCodeNo(entity.getFeatCodeNo())
			.codeType(entity.getCodeType())
			.activeStatus(entity.getActiveStatus())
			.usageStatus(entity.getUsageStatus())
			.description(entity.getDescription())
			.build();
	}
}
