package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItDataCenterService {
    String getName(String id);
    List<DataCenterVo> getList();
    List<DomainVo> getStorage(String id);
    List<NetworkVo> getNetwork(String id);
    List<ClusterVo> getCluster(String id);
    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);


    void addDatacenter(DataCenterVo dataCenterVo);   // 생성
    void editDatacenter(DataCenterVo dataCenterVo);   // 수정
    void deleteDatacenter(String id);       // 삭제


}
