package com.bing.setting.basedata.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.bing.setting.basedata.dto.CodeTranslationDTO;
import com.bing.setting.basedata.entity.CodeTranslation;
import com.bing.setting.basedata.repo.CodeTranslationRespository;
import com.bing.utils.generator.IdGeneratorServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeTranslationServiceImpl implements CodeTranslationService{

	private final CodeTranslationRespository codeTranslationRespository;
	private final IdGeneratorServiceImpl idGeneratorServiceImpl;
	
	@Override
	@Transactional
	public void createFromList(List<CodeTranslationDTO> dto) {
		List<CodeTranslation> entities = dto.stream()
				.map(this::convertToEntity).collect(Collectors.toList());
		codeTranslationRespository.saveAll(entities);
//		codeTranslationRespository.flush();
	}

	@Override
	public List<CodeTranslationDTO> getByLocaleCode(List<Integer> localeCodes,String langCode) {
		List<CodeTranslation> dbResult = codeTranslationRespository.findByListLocaleCodeAndLangCode(localeCodes,langCode);
		List<CodeTranslationDTO> result = dbResult.stream()
				.map(this::convertToResponseDto).toList();
		return result;
	}

	@Override
	public List<CodeTranslationDTO> getByLocaleCode(Integer localeCode,String langCode) {
		List<CodeTranslation> dbResult = codeTranslationRespository.findByLocaleCodeAndLangCode(localeCode,langCode);
		List<CodeTranslationDTO> result = dbResult.stream()
				.map(this::convertToResponseDto).toList();
		return result;
	}

	@Override
	@Transactional
	public Integer getNextLocaleCode() {
		try {
			Integer newLocaleCode = idGeneratorServiceImpl.getId("LOCALE_CODE");
			return newLocaleCode;
		} catch (OptimisticLockingFailureException e) {
			log.error("[SERVICE-ERROR] :: "+ Thread.currentThread().getName()+" RETRY to get new localeCode");
		}
		return null;
	}
	
	@Override
	@Transactional
	public int deleteByLocaleCode(Integer localeCode) {
		return codeTranslationRespository.deleteByLocaleCode(localeCode);
	}

	@Override
	public Boolean existsByLocaleCode(Integer localeCode) {
		return null;
	}

}
