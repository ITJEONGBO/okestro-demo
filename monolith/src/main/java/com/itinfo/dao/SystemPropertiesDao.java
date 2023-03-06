package com.itinfo.dao;

import com.itinfo.model.SystemPropertiesVo;

import org.mybatis.spring.SqlSessionTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class SystemPropertiesDao {
//	@Resource(name = "systemSqlSessionTemplate")
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public SystemPropertiesVo retrieveSystemProperties() {
		return (SystemPropertiesVo)
				this.sqlSessionTemplate.selectOne("PROPERTIES.retrieveSystemProperties");
	}

	public int updateSystemProperties(SystemPropertiesVo systemProperties) {
		return this.sqlSessionTemplate.update("PROPERTIES.updateSystemProperties", systemProperties);
	}
}