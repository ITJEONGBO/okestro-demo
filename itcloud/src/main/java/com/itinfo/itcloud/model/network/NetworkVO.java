package com.itinfo.itcloud.model.network;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class NetworkVO {

    private String id;
    private String name;

    private String status; // 클러스터에서 필요한 값

    private String comment;
    private String description;     // 설명
    private String vdsmName;        // vdsm 이름

    private boolean vmNetwork;
    private String mtu;
    private String vlanTag;

    // 네트워크 관리
    private boolean vm;
    private boolean management;
    private boolean display;
    private boolean migration;
    private boolean gluster;
    private boolean defaultRoute;


    /*vnic프로파일
    클러스터
    호스트
    가상머신
    템플릿
    권한*/

}
