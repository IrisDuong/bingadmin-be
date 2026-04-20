package com.bing.utils.eums;

import org.springframework.http.HttpHeaders;

public enum CustomHttpHeader {
	ACCEPT(HttpHeaders.ACCEPT),
	CONTENT_TYPE(HttpHeaders.CONTENT_TYPE),
    USER_AGENT(HttpHeaders.USER_AGENT),
    AUTHORIZATION(HttpHeaders.AUTHORIZATION),
	ACCEPT_LANG(HttpHeaders.ACCEPT_LANGUAGE);
    
    private final String headerName;
    
    CustomHttpHeader(String headerName){
    	this.headerName = headerName;
    }
    
    public String getHeaderName() {
    	return headerName;
    }
}
