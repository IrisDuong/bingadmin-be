package com.bing.setting.basedata.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.bing.setting.basedata.dto.CodeTranslationDTO;
import com.bing.setting.basedata.dto.RefCodeGroupDTO;
import com.bing.setting.basedata.dto.RefCodeGroupDTO.Request;
import com.bing.setting.basedata.dto.RefCodeGroupDTO.Response;
import com.bing.setting.basedata.entity.RefCodeGroup;
import com.bing.setting.basedata.repo.RefCodeGroupRespository;
import com.bing.utils.dto.PaginationResponseDTO;
import com.bing.utils.eums.ActiveStatus;
import com.bing.utils.eums.CodeType;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.eums.UsageStatus;
import com.bing.utils.exception.BaseException;
import com.bing.utils.service.LangContext;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefCodeGroupServiceImpl implements RefCodeGroupService {

	private final RefCodeGroupRespository refCodeGroupRespository;
	private final CodeTranslationService codeTranslationService;

	@Override
	@Transactional
	public void create(RefCodeGroupDTO.Request request) {
		log.info("[create ref cide group] transaction name  = "
				+ TransactionSynchronizationManager.getCurrentTransactionName());
		Boolean isExistedRefCodeGroup = refCodeGroupRespository.existsById(request.getCodeGroupNo()); // can sua
		if (Boolean.TRUE.equals(isExistedRefCodeGroup)) {
			throw new BaseException(HttpErrorCode.INTERNAL_ERROR, "Duplicate Reference Code Group");
		}

		Integer nextLocaleCode = codeTranslationService.getNextLocaleCode();

		log.info(Thread.currentThread().getName() + " is processing..with NEXT_LOCALE_CODE = " + nextLocaleCode);
		List<CodeTranslationDTO> codeTranslationSavings = request.getCodeTranslationDTOs().stream().map(dto -> {
			dto.setLocaleCode(nextLocaleCode);
			return dto;
		}).collect(Collectors.toList());
		codeTranslationService.createFromList(codeTranslationSavings);

		RefCodeGroup refCodeGroupToSave = convertToEntity(request);
		refCodeGroupToSave.setLocaleCode(nextLocaleCode);
		refCodeGroupRespository.save(refCodeGroupToSave);
	}

	@Override
	public RefCodeGroupDTO.Response getRefCodeGroupDetail(String featCodeNo, String codeGroupNo) {
		RefCodeGroup dbResult = refCodeGroupRespository
				.findByFeatCodeNoAndCodeGroupNoAndActiveStatus(featCodeNo, codeGroupNo, ActiveStatus.ACTIVE)
				.orElseThrow(() -> new BaseException(HttpErrorCode.NOT_FOUND, "Not found this Reference Code Group"));

		List<CodeTranslationDTO> codeTranslationDTOs = codeTranslationService.getByLocaleCode(dbResult.getLocaleCode(),
				null);
		
		Response dtoResult = convertToResponseDto(dbResult);
		dtoResult.setCodeTranslationDTOs(codeTranslationDTOs);
		return dtoResult;
	}

	@Override
	@Transactional
	public void deleteRefCodeGroup(String featCodeNo, String codeGroupNo) {
		RefCodeGroup existedRefCodeGroup = refCodeGroupRespository.findByFeatCodeNoAndCodeGroupNo(featCodeNo, codeGroupNo)
				.orElseThrow(() -> new BaseException(HttpErrorCode.NOT_FOUND, "Not found this Reference Code Group"));
		refCodeGroupRespository.delete(existedRefCodeGroup);
		try {
			Integer localeCode = existedRefCodeGroup.getLocaleCode();
			int deleteCodeTransResult = codeTranslationService.deleteByLocaleCode(localeCode);
			if (deleteCodeTransResult == 0)
				throw new BaseException(HttpErrorCode.INTERNAL_ERROR, "Delete Reference Code Group failed");
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			throw new BaseException(HttpErrorCode.INTERNAL_ERROR, e.getMessage());
		}

	}

	@Override
	public PaginationResponseDTO<RefCodeGroupDTO.Response> search(RefCodeGroupDTO.Request request) {
		Specification<RefCodeGroup> specification = (root, query, cb) -> {
			List<Predicate> predicates = List.of(
					cb.or(
						cb.isNotNull(root.get("featCodeNo")),
						cb.equal(root.get("featCodeNo"), request.getFeatCodeNo())
					),
					cb.or(
							cb.isNotNull(root.get("codeGroupNo")),
							cb.equal(root.get("codeGroupNo"), request.getCodeGroupNo())
					),
					cb.equal(root.get("codeType"), CodeType.getByValue(request.getCodeTypeValue())),
					cb.equal(root.get("usageStatus"), UsageStatus.getByValue(request.getUsageStatusValue())),
					cb.equal(root.get("activeStatus"), ActiveStatus.ACTIVE));
			return cb.and(predicates.toArray(new Predicate[0]));
		};

		Pageable pageable = PageRequest.of(request.getPagination().pageIndex(), request.getPagination().size());
		Page<RefCodeGroup> resultPages = refCodeGroupRespository.findAll(specification, pageable);
		
		List<RefCodeGroup> searchedRefCodeGroups = resultPages.getContent();

		List<Integer> listLocaleCodes = searchedRefCodeGroups.stream().map(RefCodeGroup::getLocaleCode).toList();
		Map<Integer, List<CodeTranslationDTO>> mapCodeTransDTO = codeTranslationService
				.getByLocaleCode(listLocaleCodes, LangContext.get()).stream()
				.collect(Collectors.groupingBy(CodeTranslationDTO::getLocaleCode));

		List<RefCodeGroupDTO.Response> refCodeGroupsResponseDTO = searchedRefCodeGroups.stream().map(entity -> {
			RefCodeGroupDTO.Response dtoResponse = this.convertToResponseDto(entity);
			dtoResponse.setCodeTranslationDTOs(mapCodeTransDTO.get(entity.getLocaleCode()));
			return dtoResponse;
		}).toList();
		
		return new PaginationResponseDTO<RefCodeGroupDTO.Response>(resultPages,refCodeGroupsResponseDTO);
	}

	@Override
	@Transactional
	public RefCodeGroupDTO.Response update(Request request) {
		RefCodeGroup existedRefCodeGroup = refCodeGroupRespository.findByFeatCodeNoAndCodeGroupNo(request.getFeatCodeNo(), request.getCodeGroupNo())
				.orElseThrow(() -> new BaseException(HttpErrorCode.NOT_FOUND, "Not found this Reference Code Group"));
		try {
			// remove old code translation
			Integer localeCode = existedRefCodeGroup.getLocaleCode();
			int deleteCodeTransResult = codeTranslationService.deleteByLocaleCode(localeCode);
			if (deleteCodeTransResult == 0)
				throw new BaseException(HttpErrorCode.INTERNAL_ERROR);
			
			// create new  code translation
			Integer nextLocaleCode = codeTranslationService.getNextLocaleCode();
			List<CodeTranslationDTO> codeTranslationDTOs = request.getCodeTranslationDTOs().stream()
					.map(dto->{
						dto.setLocaleCode(nextLocaleCode);
						return dto;
					}).toList();
			codeTranslationService.createFromList(codeTranslationDTOs);

			
			existedRefCodeGroup.setUsageStatus(UsageStatus.getByValue(request.getUsageStatusValue()));
			existedRefCodeGroup.setLocaleCode(nextLocaleCode);
			existedRefCodeGroup.setDescription(request.getDescription());
			existedRefCodeGroup = refCodeGroupRespository.save(existedRefCodeGroup);
			RefCodeGroupDTO.Response updateResponse =  this.convertToResponseDto(existedRefCodeGroup);
			updateResponse.setCodeTranslationDTOs(codeTranslationDTOs);
			
			return updateResponse;
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			throw new BaseException(HttpErrorCode.INTERNAL_ERROR, e.getMessage());
		}
	}
}
