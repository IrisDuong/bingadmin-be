package com.bing.setting.basedata.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class CodeTranslationPK implements Serializable{

	@Column(name = "locale_code")
	private Integer localeCode;
	
	@Column(name = "lang_code")
	private String langCode;
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(null == o || this.getClass() != o.getClass()) return false;
		CodeTranslationPK other = (CodeTranslationPK) o;
		return Objects.equals(langCode, other.langCode) && Objects.equals(localeCode, other.localeCode);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(langCode,localeCode);
	}
}
