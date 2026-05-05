package com.bing.utils.func;

public class JpaUtils {

	private JpaUtils() {
		super();
	}

	public static String likeExp(String value) {
		return "%".concat(value).concat("%");
	}
}
