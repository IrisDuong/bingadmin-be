package com.bing.setting.basedata.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bing.setting.basedata.dto.CodeTranslationDTO;
import com.bing.setting.basedata.dto.RefCodeItemDTO;
import com.bing.setting.basedata.service.RefCodeItemService;
import com.bing.utils.dto.ApiResponse;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.exception.BaseException;
import com.bing.utils.func.ApiUtils;
import com.bing.utils.func.CommonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ref-code-item")
@RequiredArgsConstructor
@Slf4j
public class RefCodeItemController {

	private final RefCodeItemService refCodeItemService;
	
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Boolean>> createRefCodeGroup(@RequestBody List<RefCodeItemDTO.Request> requests) throws InterruptedException{
		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(requests)))
			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
		refCodeItemService.createByList(requests);
		return ApiUtils.buildApiResponse(true, HttpStatus.CREATED, "Create Reference Code Items successfully");
	}
	
//	@GetMapping("/detail")
//	public ResponseEntity<ApiResponse<RefCodeGroupDTO.Response>> getRefCodeGroupDetail(@RequestParam String featCodeNo,@RequestParam String codeGroupNo){
//		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(codeGroupNo)))
//			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
//		RefCodeGroupDTO.Response dtoResponse = refCodeGroupService.getRefCodeGroupDetail(featCodeNo, codeGroupNo);
//		return ApiUtils.buildApiResponse(dtoResponse, HttpStatus.OK, "Found out Reference Code Group");
//	}
//	
//	@DeleteMapping("/delete")
//	public ResponseEntity<ApiResponse<Boolean>> deleteRefCodeGroup(@RequestParam String featCodeNo,@RequestParam String codeGroupNo){
//		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(codeGroupNo)))
//			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
//		refCodeGroupService.deleteRefCodeGroup(featCodeNo, codeGroupNo);
//		return ApiUtils.buildApiResponse(true, HttpStatus.OK, "Delete Reference Code Group successfully");
//	}
//
//	
//	@PostMapping("/search")
//	public ResponseEntity<ApiResponse<PaginationResponseDTO<RefCodeGroupDTO.Response>>> search(@RequestBody RefCodeGroupDTO.Request request){
//		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(request)))
//			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
//		var response = refCodeGroupService.search(request);
//		return ApiUtils.buildApiResponse(response, HttpStatus.OK, "Found out List Reference Code Group");
//	}
//	
//	@PatchMapping("/update")
//	public ResponseEntity<ApiResponse<RefCodeGroupDTO.Response>> update(@RequestBody RefCodeGroupDTO.Request request){
//		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(request)))
//			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
//		var response = refCodeGroupService.update(request);
//		return ApiUtils.buildApiResponse(response, HttpStatus.OK, "Update Reference Code Group successfully");
//	}
}
