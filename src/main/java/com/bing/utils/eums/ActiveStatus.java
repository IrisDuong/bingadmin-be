package com.bing.utils.eums;
import java.util.Arrays;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)

public enum ActiveStatus {
	ACTIVE(1,"Active"),
	INACTIVE(2,"In Active");
	
	private final Integer value;
	private final String label;
	
	public static ActiveStatus getByValue(Integer value) {
		return Arrays.stream(ActiveStatus.values())
		.filter(entry-> entry.getValue().equals(value))
		.findFirst().orElse(null);
	}
	
	private ActiveStatus(Integer value, String label) {
		this.value = value;
		this.label = label;
	}

	public Integer getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
}
