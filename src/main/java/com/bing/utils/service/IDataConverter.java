package com.bing.utils.service;

public interface IDataConverter<E,RP,RQ> {
	RP convertToResponseDto(E entity);
	E convertToEntity(RQ request);
}
