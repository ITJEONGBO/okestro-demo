package com.itinfo.dao;

import com.itinfo.model.*;

import java.util.List;
import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardDao {

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    public List<HostVo> retrieveHosts(List<String> ids) {
        return this.sqlSessionTemplate.selectList("DASHBOARD.retrieveHosts", ids);
    }

    public List<StorageVo> retireveStorages(List<String> storageIds) {
        return this.sqlSessionTemplate.selectList("DASHBOARD.retrieveStorages", storageIds);
    }

    public List<HostInterfaceVo> retrieveHostsInterface(List<String> interfaceIds) {
        return this.sqlSessionTemplate.selectList("DASHBOARD.retrieveHostsInterface", interfaceIds);
    }
}

