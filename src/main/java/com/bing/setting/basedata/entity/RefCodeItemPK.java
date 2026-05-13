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
public class RefCodeItemPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "code_group_no")
	private String codeGroupNo;
	
	@Column(name = "code_item_no")
	private String codeItemNo;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefCodeItemPK other = (RefCodeItemPK) obj;
		return Objects.equals(codeGroupNo, other.codeGroupNo) && Objects.equals(codeItemNo, other.codeItemNo);
	}
	@Override
	public int hashCode() {
		return Objects.hash(codeGroupNo, codeItemNo);
	}
	
	
}
