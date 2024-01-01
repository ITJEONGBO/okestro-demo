package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DataCenterVo {
    private String id;
    private String name;
    private String comment;
    private String description;
    private boolean local;
    private String quotaMode;
    private String status;
    private boolean storageType;
    private String supportedVersion;
    private String version;

//    private List<StorageDomainVo> storageDomainVoList;
//    private List<NetworkVo> networkVoList;
//    private List<ClusterVo> clusterVoList;



    // 리스트-> 상태, 이름(id), 코멘트, 스토리지 유형, 상태, 호환버전, 설명
    // 스토리지-> 상태, 도메인 이름, 도메인 유형, 상태, 여유공간, 사용된공간, 전체공간, 설명
    // 논리네트워크-> 이름(id), 설명
    // 클러스터-> 이름(id), 호환버전, 설명
    // 권한 -> 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, inherited from


    // LINK: storagedomains, permissions, clusters, networks, quotas, qoss, iscsibonds
    // macpool
}
