package com.itinfo.dao;

import com.itinfo.model.HostHaVo;
import com.itinfo.model.HostSwVo;
import com.itinfo.model.HostUsageVo;
import com.itinfo.model.NicUsageVo;
import com.itinfo.model.VmUsageVo;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ClustersDao {
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	@Resource(name = "sqlSessionTemplateEngine")
	private SqlSessionTemplate sqlSessionTemplateEngine;

	public List<HostUsageVo> retrieveClusterUsage(List<String> ids) {
		return this.sqlSessionTemplate.selectList("COMPUTE-CLUSTER.retrieveClusterChartUsage", ids);
	}

	public List<HostUsageVo> retrieveHostUsage(String hostId) {
		return this.sqlSessionTemplate.selectList("COMPUTE-CLUSTER.retrieveHostChartUsage", hostId);
	}

	public HostUsageVo retrieveHostLastUsage(String hostId) {
		return (HostUsageVo)this.sqlSessionTemplate.selectOne("COMPUTE-CLUSTER.retrieveHostLastUsage", hostId);
	}

	public NicUsageVo retrieveHostNicUsage(String nicId) {
		return (NicUsageVo)this.sqlSessionTemplate.selectOne("COMPUTE-CLUSTER.retrieveHostInterfaceLastUsage", nicId);
	}

	public VmUsageVo retrieveVmUsage(String vi) {
		return (VmUsageVo)this.sqlSessionTemplate.selectOne("COMPUTE-CLUSTER.retrieveVmLastUsage", vi);
	}

	public NicUsageVo retrieveVmNicUsage(String nicId) {
		return (NicUsageVo)this.sqlSessionTemplate.selectOne("COMPUTE-CLUSTER.retrieveVmInterfaceLastUsage", nicId);
	}

	public HostSwVo retrieveHostSw(String hostId) {
		return (HostSwVo)this.sqlSessionTemplate.selectOne("COMPUTE-CLUSTER.retrieveHostSw", hostId);
	}

	public List<HostHaVo> retrieveHostHaInfo() {
		return this.sqlSessionTemplateEngine.selectList("COMPUTE-CLUSTER.retrieveHostHaInfo");
	}
}
