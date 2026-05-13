package com.bing.setting.basedata.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bing.setting.basedata.entity.CodeTranslation;
import com.bing.setting.basedata.entity.CodeTranslationPK;

public interface CodeTranslationRespository extends JpaRepository<CodeTranslation, CodeTranslationPK>{

	@Query("SELECT CT FROM CodeTranslation CT WHERE CT.id.localeCode = :localeCode AND (CT.id.langCode = :langCode OR :langCode IS NULL OR :langCode = '')")
	List<CodeTranslation> findByLocaleCodeAndLangCode(Integer localeCode,String langCode);

	@Query("SELECT CT FROM CodeTranslation CT WHERE CT.id.localeCode IN :localeCodes AND (CT.id.langCode = :langCode OR :langCode IS NULL OR :langCode = '')")
	List<CodeTranslation> findByListLocaleCodeAndLangCode(List<Integer> localeCodes,String langCode);
	
	@Modifying
	@Query("DELETE FROM CodeTranslation WHERE id.localeCode = :localeCode")
	int deleteByLocaleCode(Integer localeCode);
}
