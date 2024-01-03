package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.network.NetworkVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ClusterVo {
    private String id;
    private String name;
    private String description;
    private String comment;
    private boolean ballooningEnabled;
    private String biosType;
    private String cpuType;     // cpu().type()
    private boolean threadsAsCore;
    private int memoryOverCommit;
    private boolean restoration;
    private String chipsetFirmwareType;
    private String version;

    private String datacenterId;
    private String datacenterName;

    private int hostCnt;
    private int hostUpCnt;
    private int hostDownCnt;

    private int vmCnt;
    private int vmUpCnt;
    private int vmDownCnt;


    // 리스트-> 상태, 이름(id), 코멘트, 호환버전, 설명, 클러스터cpu유형, 호스트수, 가상머신수, 업그레이드 상태
    // 일반-> 이름, 설명, 데이터센터명, 호환버전, 클러스터id, 클러스터cpu유형, 스레드를cpu로 사용, 최대메모리오버커밋, 복구정책, 칩셋유형, 가상머신수
    // 논리네트워크-> 이름(id), 상태, 역할, 설명
    // 호스트-> 이름(id), 호스트이름/ip, 상태, 불러오기(up가상머신수)
    // 가상머신-> 이름(id), 상태, 업타임, cpu,memory,network, ip주소
    // 선호도그룹-> 상태, 이름, 설명, 우선순위, 가상머신&호스트 측 극성, 가상머신&호스트 강제적용, 가상머신&호스트 멤버,가상머신&호스트 레이블
    // 선호도레이블-> 이름, 가상머신&호스트 멤버
    // cpu프로파일(권한)-> 이름, 설명, qos이름
    // 권한 -> 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, inherited from


    // LINK : permissions, cpuprofiles, networkfilters, networks, affinitygroups, glusterhooks, glustervolumes, enabledfeatures, externalnetworkproviders
    // datacenter, mac_pool , scheduling_policy
}
