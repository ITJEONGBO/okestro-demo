package com.itinfo.itcloud.dao.impl;

import com.itinfo.itcloud.dao.DashboardDAO;
import com.itinfo.itcloud.model.HostInterfaceVO;
import com.itinfo.itcloud.model.computing.HostVO;
import com.itinfo.itcloud.model.storage.DomainVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardDAOImpl implements DashboardDAO {

    @Autowired
    private SqlSession SqlSession;

    @Override
    public List<HostVO> retrieveHosts(List<String> ids) {
        return SqlSession.selectList("Dashboard.retrieveHosts", ids);
    }

    @Override
    public List<DomainVO> retireveStorages(List<String> storageIds) {
        return SqlSession.selectList("Dashboard.retrieveStorages", storageIds);
    }

    @Override
    public List<HostInterfaceVO> retrieveHostsInterface(List<String> interfaceIds) {
        return SqlSession.selectList("Dashboard.retrieveHostsInterface", interfaceIds);
    }


}
