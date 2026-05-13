package com.bing.setting.basedata.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SETTING_REF_CODE_GROUP")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class RefCodeGroup extends BaseCode{

	@Id
	@Column(name = "code_group_no")
	private String codeGroupNo;
	
	@OneToMany(mappedBy = "refCodeGroup", fetch = FetchType.LAZY)
	private List<RefCodeItem> refCodeItems;
}
