package com.itinfo.dao;

import com.itinfo.model.VmDeviceVo;
import com.itinfo.model.VmNetworkUsageVo;
import com.itinfo.model.VmUsageVo;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ComputingDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public List<VmUsageVo> retrieveVmUsage(String id) {
		return this.sqlSessionTemplate.selectList("COMPUTING.retrieveVmUsage", id);
	}

	public VmUsageVo retrieveVmUsageOne(String id) {
		return (VmUsageVo)this.sqlSessionTemplate.selectOne("COMPUTING.retrieveVmUsageOne", id);
	}

	public List<VmNetworkUsageVo> retrieveVmNetworkUsage(List<String> ids) {
		return this.sqlSessionTemplate.selectList("COMPUTING.retrieveVmNetworkUsage", ids);
	}

	public VmNetworkUsageVo retrieveVmNetworkUsageOne(List<String> ids) {
		return (VmNetworkUsageVo)this.sqlSessionTemplate.selectOne("COMPUTING.retrieveVmNetworkUsageOne", ids);
	}

	public List<VmDeviceVo> retrieveVmDevices(String id) {
		return this.sqlSessionTemplate.selectList("COMPUTING.retrieveVmDevices", id);
	}
}
