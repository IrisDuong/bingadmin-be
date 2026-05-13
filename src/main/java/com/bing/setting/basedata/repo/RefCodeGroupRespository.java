package com.bing.setting.basedata.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bing.setting.basedata.dto.RefCodeGroupDTO;
import com.bing.setting.basedata.entity.RefCodeGroup;
import com.bing.utils.eums.ActiveStatus;

public interface RefCodeGroupRespository extends JpaRepository<RefCodeGroup, String>, JpaSpecificationExecutor<RefCodeGroup>{

	Optional<RefCodeGroup> findByFeatCodeNoAndCodeGroupNo(String featCodeNo,String codeGroupNo);
	Optional<RefCodeGroup> findByFeatCodeNoAndCodeGroupNoAndActiveStatus(String featCodeNo,String codeGroupNo,ActiveStatus activeStatus);
}
