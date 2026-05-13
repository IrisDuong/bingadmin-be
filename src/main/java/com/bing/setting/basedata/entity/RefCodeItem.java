package com.bing.setting.basedata.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SETTING_REF_CODE_ITEM")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class RefCodeItem extends BaseCode{
	
	@EmbeddedId
	private RefCodeItemPK id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("codeGroupNo")
	@JoinColumn(name = "code_group_no")
	private RefCodeGroup refCodeGroup;
}
