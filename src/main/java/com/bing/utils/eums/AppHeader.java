package com.bing.utils.eums;

import org.springframework.http.HttpHeaders;

public enum AppHeader {
	ACCEPT(HttpHeaders.ACCEPT),
	CONTENT_TYPE(HttpHeaders.CONTENT_TYPE),
    USER_AGENT(HttpHeaders.USER_AGENT),
    AUTHORIZATION(HttpHeaders.AUTHORIZATION);
    
    private final String headerName;
    
    AppHeader(String headerName){
    	this.headerName = headerName;
    }
    
    public String getHeaderName() {
    	return headerName;
    }
}
