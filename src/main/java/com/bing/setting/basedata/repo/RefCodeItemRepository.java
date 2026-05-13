package com.bing.setting.basedata.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bing.setting.basedata.entity.RefCodeItem;
import com.bing.setting.basedata.entity.RefCodeItemPK;
import com.bing.utils.eums.ActiveStatus;

public interface RefCodeItemRepository extends JpaRepository<RefCodeItem, RefCodeItemPK>{

	@Query("""
			SELECT CASE WHEN COUNT(RCI) > 0 THEN true ELSE false END FROM RefCodeItem RCI
			WHERE RCI.featCodeNo = :featCodeNo 
				AND  RCI.id.codeGroupNo = :codeGroupNo 
				AND RCI.id.codeItemNo IN (:listOfCodeItemNo)
				AND RCI.activeStatus = :activeStatus
			""")
	Boolean existsByListCodeItemNo(String featCodeNo,String codeGroupNo,List<String> listOfCodeItemNo,ActiveStatus activeStatus);
}
