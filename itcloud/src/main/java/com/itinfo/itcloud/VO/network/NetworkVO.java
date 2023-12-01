package com.itinfo.itcloud.VO.network;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NetworkVO {

    private String networkId;
    private String networkName;

    private String comment;
    private String description;     // 설명
    private String vdsmName;        // vdsm 이름

    private boolean vmNetwork;
    private String mtu;
    private String vlanTag;

    /*vnic프로파일
    클러스터
    호스트
    가상머신
    템플릿
    권한*/

}
