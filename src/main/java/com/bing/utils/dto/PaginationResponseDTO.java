package com.bing.utils.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PaginationResponseDTO<T> {
	private List<T> content;
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;
    private boolean hasNext;
    
    public PaginationResponseDTO(Page page,List<T> content) {
    	this.content = content;
    	this.currentPage = page.getNumber();
    	this.pageSize = page.getSize();
    	this.totalItems = page.getTotalElements();
    	this.totalPages = page.getTotalPages();
    	this.hasNext = page.hasNext();
    }
    
}
