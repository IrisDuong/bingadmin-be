package com.bing.setting.basedata.entity;

import com.bing.utils.service.BaseAuditing;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SYS_CODE_TRANSLATION")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class CodeTranslation  extends BaseAuditing{

	@EmbeddedId
	private CodeTranslationPK id;
	
	@Column(name = "locale_name", columnDefinition = "NVARCHAR(255)")
	private String localeName;
}
