package com.bing.setting.basedata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bing.setting.basedata.dto.CodeTranslationDTO;
import com.bing.setting.basedata.dto.RefCodeItemDTO;
import com.bing.setting.basedata.dto.RefCodeItemDTO.Request;
import com.bing.setting.basedata.entity.RefCodeGroup;
import com.bing.setting.basedata.entity.RefCodeItem;
import com.bing.setting.basedata.repo.RefCodeGroupRespository;
import com.bing.setting.basedata.repo.RefCodeItemRepository;
import com.bing.utils.eums.ActiveStatus;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.exception.BaseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefCodeItemServiceImpl implements RefCodeItemService{

	private final CodeTranslationService codeTranslationService;
	private final RefCodeItemRepository refCodeItemRepository;
	private final RefCodeGroupRespository refCodeGroupRespository;
	
	@Override
	public void createByList(List<RefCodeItemDTO.Request> requests) {
		RefCodeItemDTO.Request firstRequest = requests.get(0);
		List<String> listOfCodeItemNo = requests.stream().map(RefCodeItemDTO.Request::getCodeItemNo).toList();
		Boolean isExistedRefCodeItem = refCodeItemRepository.existsByListCodeItemNo(firstRequest.getFeatCodeNo(), firstRequest.getCodeGroupNo(), listOfCodeItemNo, ActiveStatus.ACTIVE);
		if(Boolean.TRUE.equals(isExistedRefCodeItem))
			throw new BaseException(HttpErrorCode.DUPLICATED_DATA,"Duplicate Reference Code Item");

		try {
			RefCodeGroup ownerRefCodeGroup = refCodeGroupRespository.findByFeatCodeNoAndCodeGroupNoAndActiveStatus(firstRequest.getFeatCodeNo(), firstRequest.getCodeGroupNo(), ActiveStatus.ACTIVE)
					.orElseThrow(() -> new BaseException(HttpErrorCode.NOT_FOUND, "Not found this Reference Code Group"));
			
			List<RefCodeItem> refCodeItemSavings = requests.stream()
					.map(requestItem->{
				Integer nextLocaleCode = codeTranslationService.getNextLocaleCode();
				List<CodeTranslationDTO> codeTranslationSavings = requestItem.getCodeTranslationDTOs().stream()
						.map(dto->{
							dto.setLocaleCode(nextLocaleCode);
							return dto;
						}).toList();
				codeTranslationService.createFromList(codeTranslationSavings);
				
				RefCodeItem refCodeItem = this.convertToEntity(requestItem);
				refCodeItem.setLocaleCode(nextLocaleCode);
				refCodeItem.setRefCodeGroup(ownerRefCodeGroup);
				
				return refCodeItem;
			}).toList();
			refCodeItemRepository.saveAll(refCodeItemSavings);
		} catch (RuntimeException e) {
			throw new BaseException(HttpErrorCode.INTERNAL_ERROR);
		}
	}
}
