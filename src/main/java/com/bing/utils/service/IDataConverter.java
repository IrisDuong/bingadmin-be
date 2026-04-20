package com.bing.utils.service;

public interface IDataConverter<E,D> {
	D convertToDto(E e);
	E convertToEntity(D d);
}
