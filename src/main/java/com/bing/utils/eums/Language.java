package com.bing.utils.eums;

import java.util.Arrays;

public enum Language {

	VI("vi","Vietnam"),
	EN("en","English");
	
	private final String langCode;
	private final String langName;
	
	private Language(String langCode, String langName) {
		this.langCode = langCode;
		this.langName = langName;
	}
	
	public static Language getByCode(String langCode) {
		return Arrays.stream(Language.values())
		.filter(entry-> entry.getLangCode().equals(langCode))
		.findFirst().orElse(null);
	}

	public String getLangCode() {
		return langCode;
	}

	public String getLangName() {
		return langName;
	}
	
	
}
