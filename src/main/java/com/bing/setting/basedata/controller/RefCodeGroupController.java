package com.bing.setting.basedata.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bing.setting.basedata.dto.CodeTranslationDTO;
import com.bing.setting.basedata.dto.RefCodeGroupDTO;
import com.bing.setting.basedata.service.RefCodeGroupService;
import com.bing.utils.dto.ApiResponse;
import com.bing.utils.dto.PaginationResponseDTO;
import com.bing.utils.eums.HttpErrorCode;
import com.bing.utils.exception.BaseException;
import com.bing.utils.func.ApiUtils;
import com.bing.utils.func.CommonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ref-code-group")
@RequiredArgsConstructor
@Slf4j
public class RefCodeGroupController {

	private final RefCodeGroupService refCodeGroupService;
	
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Boolean>> createRefCodeGroup(@RequestBody RefCodeGroupDTO.Request request) throws InterruptedException{
		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(request)))
			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
		refCodeGroupService.create(request);
		return ApiUtils.buildApiResponse(true, HttpStatus.CREATED, "Create Reference Code Group successfully");
	}
	
	@GetMapping("/detail")
	public ResponseEntity<ApiResponse<RefCodeGroupDTO.Response>> getRefCodeGroupDetail(@RequestParam String featCodeNo,@RequestParam String codeGroupNo){
		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(codeGroupNo)))
			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
		RefCodeGroupDTO.Response dtoResponse = refCodeGroupService.getRefCodeGroupDetail(featCodeNo, codeGroupNo);
		return ApiUtils.buildApiResponse(dtoResponse, HttpStatus.OK, "Found out Reference Code Group");
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse<Boolean>> deleteRefCodeGroup(@RequestParam String featCodeNo,@RequestParam String codeGroupNo){
		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(codeGroupNo)))
			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
		refCodeGroupService.deleteRefCodeGroup(featCodeNo, codeGroupNo);
		return ApiUtils.buildApiResponse(true, HttpStatus.OK, "Delete Reference Code Group successfully");
	}

	
	@PostMapping("/search")
	public ResponseEntity<ApiResponse<PaginationResponseDTO<RefCodeGroupDTO.Response>>> search(@RequestBody RefCodeGroupDTO.Request request){
		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(request)))
			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
		var response = refCodeGroupService.search(request);
		return ApiUtils.buildApiResponse(response, HttpStatus.OK, "Found out List Reference Code Group");
	}
	
	@PatchMapping("/update")
	public ResponseEntity<ApiResponse<RefCodeGroupDTO.Response>> update(@RequestBody RefCodeGroupDTO.Request request){
		if(Boolean.TRUE.equals(CommonUtils.isEmptyData(request)))
			throw new BaseException(HttpErrorCode.INVALID_REQUEST);
		var response = refCodeGroupService.update(request);
		return ApiUtils.buildApiResponse(response, HttpStatus.OK, "Update Reference Code Group successfully");
	}
	@GetMapping("/create/test")
	public ResponseEntity<ApiResponse<Boolean>> testCreateRefCodeGroup() throws InterruptedException{
		ExecutorService executor = Executors.newFixedThreadPool(3);
		
		RefCodeGroupDTO.Request request01 = new RefCodeGroupDTO.Request();
		request01.setCodeGroupNo("INV01");
		List<CodeTranslationDTO> codeTranslationDTOs01 = new ArrayList<CodeTranslationDTO>();
		codeTranslationDTOs01.add(CodeTranslationDTO.builder().langCode("vi").localeName("Mã Kệ").build());
		codeTranslationDTOs01.add(CodeTranslationDTO.builder().langCode("en").localeName("Rack No").build());
		request01.setCodeTranslationDTOs(codeTranslationDTOs01);

		RefCodeGroupDTO.Request request02 = new RefCodeGroupDTO.Request();
		request02.setCodeGroupNo("INV02");
		List<CodeTranslationDTO> codeTranslationDTOs02 = new ArrayList<CodeTranslationDTO>();
		codeTranslationDTOs02.add(CodeTranslationDTO.builder().langCode("vi").localeName("Trạng Thái Nhập Kho").build());
		codeTranslationDTOs02.add(CodeTranslationDTO.builder().langCode("en").localeName("Inbound Status").build());
		request02.setCodeTranslationDTOs(codeTranslationDTOs02);

		RefCodeGroupDTO.Request request03 = new RefCodeGroupDTO.Request();
		request03.setCodeGroupNo("INV03");
		List<CodeTranslationDTO> codeTranslationDTOs03 = new ArrayList<CodeTranslationDTO>();
		codeTranslationDTOs03.add(CodeTranslationDTO.builder().langCode("vi").localeName("Đơn Vị Tính").build());
		codeTranslationDTOs03.add(CodeTranslationDTO.builder().langCode("en").localeName("Unit of measurement").build());
		request03.setCodeTranslationDTOs(codeTranslationDTOs03);
		
		RefCodeGroupDTO.Request request04 = new RefCodeGroupDTO.Request();
		request04.setCodeGroupNo("INV04");
		List<CodeTranslationDTO> codeTranslationDTOs04 = new ArrayList<CodeTranslationDTO>();
		codeTranslationDTOs04.add(CodeTranslationDTO.builder().langCode("vi").localeName("Loại Phiếu Nhập Kho").build());
		codeTranslationDTOs04.add(CodeTranslationDTO.builder().langCode("en").localeName("Stock Receipt").build());
		request04.setCodeTranslationDTOs(codeTranslationDTOs04);
		
		executor.submit(()-> refCodeGroupService.create(request03));
		executor.submit(()-> refCodeGroupService.create(request01));
		executor.submit(()-> refCodeGroupService.create(request02));
		executor.submit(()-> refCodeGroupService.create(request04));
		
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		return ApiUtils.buildApiResponse(true, HttpStatus.CREATED, "Create Reference Code Group successfully");
	}
	
}
