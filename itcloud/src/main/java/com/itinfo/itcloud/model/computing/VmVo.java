package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.network.VnicProfileVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Getter @Builder @ToString
public class VmVo {
    private String id;
    private String name;
    private String status;
    private String description;
    private String fqdn;
    private String upTime;

    private String osSystem;
    private String chipsetFirmwareType; // bios.type
    private int priority;    // 고가용성 HighAvailability
    private String optimizeOption;

    private BigInteger memory;
    private BigInteger memoryActual;
    private int cpuCoreCnt;
    private int cpuTopologyCore;
    private int cpuTopologySocket;
    private int cpuTopologyThread;
    private int guestCpuCnt;
    private String guestCpu;
    private boolean ha;
    private int monitor;
    private boolean usb;
    private String hwTimeOffset;
    //작성자
    // 소스
    // 실행 호스트
    // 사용자 정의속성
    // 클러스터 호환버전

    private BigInteger guestMemory;
    private BigInteger guestBufferedMemory;


    private String datacenterId;
    private String datacenterName;
    private String clusterId;
    private String clusterName;
    private String hostId;
    private String hostName;
    private String templateID;
    private String templateName;
    private String ipv4;
    private String ipv6;


//    private List<VnicProfileVo> vnicProfileVoList;
//    private List<VmDiskVo> vmDiskVoList;

    // 리스트-> 상태, 이름(id), 호스트(id), ip주소, fqdn, 클러스터(id), 데이터센터(id)
    //          메모리,cpu,네트워크, 상태, 업타임, 설명
    // 일반-> 이름, 설정된 메모리, 작성자, 설명, 할당할 실제 메모리, 소스, 상태, 실행 호스트, 업타임, 게스트 OS의 여유/캐시+버퍼된 메모리
    //      사용자정의 속성, 템플릿, CPU 코어수, 클러스터 호환버전, 운영시스템, 게스트 CPU수, 가상머신의 ID, 칩셋/펌웨어 유형, 게스트CPU
    //      고가용성, 모니터수, FQDN, 우선순위, USB, 하드웨어 클럭의 시간오프셋, 최적화 옵션
    // 네트워크인터페이스-> 이름, 네트워크 이름, ipv4,ipv6, mac
    //               일반: 연결됨, 네트워크이름, 프로파일이름, 링크상태, 유형, 속도, 포트미러링, 게스트인터페이스이름
    //               통계: rx속도, 총rx, tx속도, 총tx속도, 중단
    // 디스크-> 별칭, 부팅가능, 공유가능, 읽기전용, 가상크기, 연결대상, 인터페이스, 논리적이름, 상태, 유형, 설명
    // 스냅샷-> 아 뭐가많음요
    // 애플리케이션-> 설치된 애플리케이션이름
    // 선호도그룹-> 상태, 이름, 설명, 우선순위, 가상머신&호스트 측 극성, 가상머신&호스트 강제적용, 가상머신&호스트 멤버,가상머신&호스트 레이블
    // 선호도 레이블-> 이름, 가상머신 멤버, 호스트 멤버
    // 게스트정보-> 유형, 아키텍처, 운영체제, 커널버전, 시간대, 로그인된사용자, 콘솔사용자, 콘솔클라이언트, ip
    // 권한 -> 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, inherited from

    // LINK: numanodes katelloerrata permissions tags diskattachments mediateddevices watchdogs
    //       graphicsconsoles cdroms nics affinitylabels reporteddevices sessions backups
    //       checkpoints snapshots applications hostdevices statistics

    // cluster, cpu_profile, host, original_template, template




}
