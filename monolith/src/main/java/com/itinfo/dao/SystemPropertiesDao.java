package com.itinfo.dao;

import com.itinfo.model.SystemPropertiesVo;

import org.mybatis.spring.SqlSessionTemplate;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


@Repository
public class SystemPropertiesDao {

	@Resource(name = "systemSqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	public SystemPropertiesVo retrieveSystemProperties() {
		return this.sqlSessionTemplate.selectOne("PROPERTIES.retrieveSystemProperties");
	}

	public int updateSystemProperties(SystemPropertiesVo systemProperties) {
		return this.sqlSessionTemplate.update("PROPERTIES.updateSystemProperties", systemProperties);
	}
}