package com.bing.utils.service;

public class LangContext {

	private static final ThreadLocal<String> LANG = new ThreadLocal<String>();
	
	public static String get() {
		return LANG.get();
	}
	
	public static void set(String langCode) {
		LANG.set(langCode);
	}
	
	public static void clear() {
		LANG.remove();
	}
}
