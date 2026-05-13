package com.bing.setting.basedata.service;

import java.util.List;

import com.bing.setting.basedata.dto.CodeTranslationDTO;
import com.bing.setting.basedata.entity.CodeTranslation;
import com.bing.setting.basedata.entity.CodeTranslationPK;
import com.bing.utils.service.IDataConverter;

public interface CodeTranslationService extends IDataConverter<CodeTranslation, CodeTranslationDTO,CodeTranslationDTO>{

	void createFromList(List<CodeTranslationDTO> dtos);
	List<CodeTranslationDTO> getByLocaleCode(List<Integer> localeCodes,String langCode);
	List<CodeTranslationDTO> getByLocaleCode(Integer localeCode,String langCode);
	Integer getNextLocaleCode();
	int deleteByLocaleCode(Integer localeCode);
	Boolean existsByLocaleCode(Integer localeCode);
	
	@Override
	default CodeTranslationDTO convertToResponseDto(CodeTranslation entity) {
		return CodeTranslationDTO.builder()
				.localeCode(entity.getId().getLocaleCode())
				.langCode(entity.getId().getLangCode())
				.localeName(entity.getLocaleName())
				.build();
	}
	@Override
	default CodeTranslation convertToEntity(CodeTranslationDTO dto) {
		CodeTranslationPK id = CodeTranslationPK.builder()
				.localeCode(dto.getLocaleCode())
				.langCode(dto.getLangCode())
				.build();
		return CodeTranslation.builder()
				.id(id)
				.localeName(dto.getLocaleName())
				.build();
	}
}
