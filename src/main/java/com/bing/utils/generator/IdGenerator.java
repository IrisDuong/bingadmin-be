package com.bing.utils.generator;

import com.bing.setting.basedata.entity.BaseCode;
import com.bing.utils.eums.ActiveStatus;
import com.bing.utils.eums.CodeType;
import com.bing.utils.eums.UsageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SYS_ID_GENERATOR")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdGenerator {

	@Id
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "value", nullable = false)
	private Integer value;
	
	@Version
	private Integer version;
}
