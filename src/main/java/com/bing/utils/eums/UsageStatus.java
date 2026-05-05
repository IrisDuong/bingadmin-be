package com.bing.utils.eums;

import java.util.Arrays;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum UsageStatus {

	USED(true, "Used"),
	NOT_USED(false, "Not Used");

	private final Boolean value;
	private final String label;

	public static UsageStatus getByValue(Boolean value) {
		return Arrays.stream(UsageStatus.values())
				.filter(entry -> entry.getValue().equals(value))
				.findFirst()
				.orElse(null);
	}

	private UsageStatus(Boolean value, String label) {
		this.value = value;
		this.label = label;
	}

	public Boolean getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
}
