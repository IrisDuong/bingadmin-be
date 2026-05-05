package com.bing.utils.generator;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdGeneratorServiceImpl {
	private final IdGeneratorRepository repository;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Retryable(
			value = {OptimisticLockingFailureException.class},
			maxAttempts = 5,
			backoff = @Backoff(delay = 100)
	)
	public Integer getId(String name) {
		IdGenerator id = repository.findById(name).orElseThrow();
		Integer newIdValue = id.getValue() + 1;
		id.setValue(newIdValue);
		
		return newIdValue;
	}
}
