package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.QuotaModeType;

@Getter @Setter @ToString
public class DataCenterVo {
    private String id;
    private String name;
    private String comment;
    private String description;
    private QuotaModeType quotaMode;     // QuotaMode: 비활성화됨, 감사, 강제적용
    private boolean storageType;         // local
    private String version;

    private String supportedVersion;
    private String status;


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
