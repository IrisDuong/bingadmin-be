package com.bing.setting.basedata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CodeTranslationDTO {
	private String localeName;
	private Integer localeCode;
	private String langCode;
}
