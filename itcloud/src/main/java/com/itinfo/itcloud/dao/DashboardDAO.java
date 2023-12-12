package com.itinfo.itcloud.dao;

import com.itinfo.itcloud.model.HostInterfaceVO;
import com.itinfo.itcloud.model.computing.HostVO;
import com.itinfo.itcloud.model.storage.DomainVO;

import java.util.List;

public interface DashboardDAO {

    List<HostVO> retrieveHosts(List<String> ids);

    List<DomainVO> retireveStorages(List<String> storageIds);

    List<HostInterfaceVO> retrieveHostsInterface(List<String> interfaceIds);
}
