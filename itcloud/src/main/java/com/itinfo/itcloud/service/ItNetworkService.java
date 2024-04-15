package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItNetworkService {
    List<NetworkVo> getList();


    List<NetworkDcClusterVo> setDcCluster();    // 네트워크 생성 창

    CommonVo<Boolean> addNetwork(NetworkCreateVo ncVo); // 네트워크 생성
    NetworkCreateVo setEditNetwork(String id);   // 네트워크 편집 창
    CommonVo<Boolean> editNetwork(NetworkCreateVo ncVo);  // 네트워크 편집
    CommonVo<Boolean> deleteNetwork(String id); // 네트워크 삭제

    NetworkImportVo setImportNetwork();    // 네트워크 가져오기 생성창
    CommonVo<Boolean> importNetwork();  // 네트워크 가져오기


    NetworkVo getNetwork(String id);

    List<VnicProfileVo> getVnic(String id);
    VnicCreateVo setVnic(String id);
    CommonVo<Boolean> addVnic(String id, VnicCreateVo vcVo);
    VnicCreateVo setEditVnic(String id, String vcId);
    CommonVo<Boolean> editVnic(String id, String vcId, VnicCreateVo vcVo);
    CommonVo<Boolean> deleteVnic(String id, String vcId);


    List<NetworkClusterVo> getCluster(String id);
    NetworkUsageVo getUsage(String id, String cId);
    CommonVo<Boolean> editUsage(String id, String cId, NetworkUsageVo nuVo);

    List<NetworkHostVo> getHost(String id);

    List<NetworkVmVo> getVm(String id);

    List<TemplateVo> getTemplate(String id);

    List<PermissionVo> getPermission(String id);




}
