package com.bing.setting.basedata.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bing.utils.eums.ActiveStatus;
import com.bing.utils.eums.CodeType;
import com.bing.utils.eums.UsageStatus;
import com.bing.utils.service.BaseAuditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public abstract class BaseCode extends BaseAuditing{
	protected String featCodeNo;
	protected String description;

	@Enumerated(EnumType.STRING)
	protected ActiveStatus activeStatus;

	@Enumerated(EnumType.STRING)
	protected CodeType codeType;
	
	@Enumerated(EnumType.STRING)
	protected UsageStatus usageStatus;
	
	@Column(name = "locale_code")
	private Integer localeCode;
}
