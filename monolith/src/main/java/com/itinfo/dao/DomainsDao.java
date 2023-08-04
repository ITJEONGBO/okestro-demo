package com.itinfo.dao;

import com.itinfo.model.StorageDomainUsageVo;

import java.util.List;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class DomainsDao {
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public List<StorageDomainUsageVo> retrieveStorageDomainUsage(String storageDomainId) {
		log.info("... retrieveStorageDomainUsage("+storageDomainId+")");
		return this.sqlSessionTemplate.selectList("STORAGE-DOMAINS.retrieveStorageDomainUsage", storageDomainId);
	}
}

