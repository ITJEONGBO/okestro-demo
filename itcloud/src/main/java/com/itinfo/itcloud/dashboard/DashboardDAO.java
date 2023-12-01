package com.itinfo.itcloud.dashboard;

import com.itinfo.itcloud.VO.HostInterfaceVO;
import com.itinfo.itcloud.VO.computing.HostVO;
import com.itinfo.itcloud.VO.storage.DomainVO;

import java.util.List;

public interface DashboardDAO {

    List<HostVO> retrieveHosts(List<String> ids);

    List<DomainVO> retireveStorages(List<String> storageIds);

    List<HostInterfaceVO> retrieveHostsInterface(List<String> interfaceIds);
}
