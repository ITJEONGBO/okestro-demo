package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItDataCenterService {
    String getName(String id);
    List<DataCenterVo> getList();
//    List<DomainVo> getStorage(String id);
//    List<NetworkVo> getNetwork(String id);
//    List<ClusterVo> getCluster(String id);
//    List<PermissionVo> getPermission(String id);
    List<EventVo> getEvent(String id);


    DataCenterCreateVo getDatacenter(String id);

    boolean addDatacenter(DataCenterCreateVo dcVo);   // 생성
    void editDatacenter(DataCenterCreateVo dcVo);   // 수정
    boolean deleteDatacenter(String id);       // 삭제

}
