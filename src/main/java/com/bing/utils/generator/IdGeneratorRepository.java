package com.bing.utils.generator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IdGeneratorRepository extends JpaRepository<IdGenerator, String>{

}
