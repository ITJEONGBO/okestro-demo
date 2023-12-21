package com.itinfo.itcloud.model.aaa;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NetworkVo {
    private String id;
    private String name;
    private String description;
    private String comment;
    private int mtu;
    private boolean portIsolation;
    private boolean stp;
    private String usage;  // 역할
    private String vdsmName;

    private String datacenterId;
    private String datacenterName;



    // 리스트 -> Vlan, 레이블, 공급자
    // 일반 -> 이름, id, 설명, vdsm 이름, (가상머신 네트워크), vLAN 태그, mtu
    // vnic 프로파일 -> 이름, 네트워크, 데이터센터(id), 호환버전, 네트워크 필터, 포트미러링, 통과, 페일오버 vnic, 설명
    // 클러스터 -> 이름(id), 호환버전, 연결된 네트워크, 네트워크 상태, 필수 네트워크, 네트워크 역할, 설명
    // 호스트 -> 이름(id), 클러스터, 데이터센터, 네트워크 장치상태, 비동기, 네트워크 장치, 속도, Rx, Tx, 총Rx, 총Tx
    // 가상머신 -> 이름(id), 클러스터, ip주소, fqdn, vnic상태, vnic, vnicRx, vnicTx, 총Rx, 총Tx, 설명
    // 템플릿 -> 이름, 버전, 상태, 클러스터, vnic
    // 권한 -> 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, inherited from


    // 제외: qos이름
    // LINK: permissions, networklabels, vnicprofiles
    // datacenter
}
