package com.itinfo.itcloud.model.aaa;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VnicProfileVo {
    private String id;
    private String name;
    private String passThrough;
    private boolean portMirroring;

    private String networkId;
    private String networkName;
    private String networkFilterId;
    private String networkFilterName;



    // 리스트-> 이름(id), 네트워크이름, 데이터센터(id), 호환버전, 네트워크 필터, 포트 미러링, 통과, 페일오버 vnic 프로파일, 설명
    // 가상머신-> 이름(id)
    // 템플릿-> 이름, 버전
    // 권한 -> 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, inherited from


    // Link : permissions
    // network, network_filter
}
