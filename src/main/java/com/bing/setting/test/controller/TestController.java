package com.bing.setting.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

	@GetMapping("/ssl")
	public Map<String, String> testSsl(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("scheme", request.getScheme());
		map.put("serverName", request.getServerName());
		map.put("serverPort", request.getServerPort()+"");
		return map;
	}
}
