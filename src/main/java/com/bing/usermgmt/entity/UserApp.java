package com.bing.usermgmt.entity;

import com.bing.setting.basedata.entity.CodeTranslation;
import com.bing.setting.basedata.entity.CodeTranslationPK;
import com.bing.utils.service.BaseAuditing;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SYS_USER_APP")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class UserApp {

	@Id
	private String email;
	private String firstName;
	private String lastName;
	private String avavatar;
}
