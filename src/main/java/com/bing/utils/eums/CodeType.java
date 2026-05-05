package com.bing.utils.eums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import java.util.Arrays;
import java.util.stream.Stream;
@JsonFormat(shape = Shape.OBJECT)
public enum CodeType {

	MULTI(1,"Multi"),
	SINGLE(2,"Single");

	private final Integer value;
	private final String label;
	
	public static CodeType getByValue(Integer value) {
		return Arrays.stream(CodeType.values())
		.filter(entry-> entry.getValue().equals(value))
		.findFirst().orElse(null);
	}
	
	private CodeType(Integer value, String label) {
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
