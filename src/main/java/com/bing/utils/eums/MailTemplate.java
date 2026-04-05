package com.bing.utils.eums;

import java.util.stream.Stream;
public enum MailTemplate {
	NEW_USER("NEW_USER","new-user");
	
	private final String templateCode;
	private final String templateName;
	
	private MailTemplate(String templateCode, String templateName) {
		this.templateCode = templateCode;
		this.templateName = templateName;
	}
	
	public static MailTemplate getByCode(String templateCode) {
		return Stream.of(MailTemplate.values()).filter(template->template.templateCode.equals(templateCode)).findFirst().get();
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public String getTemplateName() {
		return templateName;
	}
}
