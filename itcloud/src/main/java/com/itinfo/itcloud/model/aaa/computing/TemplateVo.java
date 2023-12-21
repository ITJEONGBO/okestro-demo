package com.itinfo.itcloud.model.aaa.computing;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter @Setter
public class TemplateVo {
    private String id;
    private String name;
    private String description;
    private String status;
    private String version;
    private Date createDate;
    private String osType;
    private String chipsetFirmwareType; // bios.type
    private String optimizeOption;
    private BigInteger memory;
    private int cpuCoreCnt;
    private int monitor;
    private boolean ha;
    private String priority;
    private boolean usb;
    // 소스
    // 상태 비저장

    private String clusterID;
    private String clusterName;
    private String datacenterId;
    private String datacenterName;



    // 리스트-> 이름, 버전, 코멘트, 생성일자, 상태, 보관, 클러스터, 데이터센터, 설명
    // 일반-> 이름, 설명, 호스트클러슽, 운영시스템, 칩셋유형, 최적화옵션, 설정된메모리,
    //       cpu코어수, 모니터수, 고가용성, 우선순위, usb, 소스, 상태비저장, 템플릿id
    // 가상머신-> 이름(id), 상태, 호스트, ip주소, fqdn, 업타임, 콘솔사용자, 로그인된 사용자
    // 네트워크인터페이스-> 이름, 연결됨, 네트워크이름, 프로파일이름, 링크상태, 유형
    // 디스크-> R/O, 가상크기, 실제크기, 상태, 할당정책, 인터페이스, 유형
    // 스토리지-> 도메인이름, 도메인유형, 상태, 여유공간, 사용된공간, 전체공간
    // 권한 -> 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, inherited from

    // LINK: permissions, tags,diskattachments,mediateddevices,watchdogs,graphicsconsoles,cdroms,nics


}
