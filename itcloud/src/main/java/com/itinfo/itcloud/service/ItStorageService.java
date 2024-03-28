package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.VolumeVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItStorageService {
    String getName(String id);

    List<DiskVo> getDiskVoList(String dcId);    // 디스크 리스트
    List<DomainVo> getDomainList();    // 도메인 리스트
    List<VolumeVo> getVolumeVoList(String dcId);   // 나중에
    List<DomainVo> getStorageList(String dcId);    // 스토리지 리스트(이게 dc있는거)
    List<NetworkVo> getNetworkVoList(String dcId);
    List<ClusterVo> getClusterVoList(String dcId);
    List<PermissionVo> getPermission(String dcId);
    List<EventVo> getEvent(String id);
}
