package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter @Builder
public class HostVo {
    private String id;
    private String name;
    private String comment;
    private String address;
    private String status;

    // cpu 소켓
    private int cpuTopologyCore;
    private int cpuTopologySocket;
    private int cpuTopologyThread;
    private int cpuCnt;
    private List<Integer> cpuOnline;

    private boolean devicePassThrough;
    private String iscsi;
    private String kdump;       // kdumpStatus(Enum): disabled, enabled, unknown

    private String bootingTime;

    // 물리적 메모리
    private BigInteger memory;
    private BigInteger memoryUsed;
    private BigInteger memoryFree;
    private BigInteger memoryMax; // 새 가상머신 최대여유메모리
    private BigInteger memoryShared;    // 공유 메모리

    // swap 크기
    private BigInteger swapTotal;
    private BigInteger swapUsed;
    private BigInteger swapFree;


    private String seLinux;
    private String spmStatus;   // spm 상태
    private int spmPriority;    // spm 우선순위

    private Boolean hostedEngine;   // Hosted Engine HA [ 금장, 은장, null ]
    private Boolean hostedActive;    // 활성여부
    private int hostedScore;        // 점수

    private boolean ksm;             // 메모리 페이지 공유
    private boolean pageSize;       // 자동으로 페이지를 크게


    // Huge Pages (size: free/total)
    // 2048:0/0, 1048576:0/0
    private int hugePage2048Free;
    private int hugePage2048Total;
    private int hugePage1048576Free;
    private int hugePage1048576Total;

    // 클러스터 호환 버전
    private String clusterVer;

    private String clusterId;
    private String clusterName;
    private String datacenterId;
    private String datacenterName;


    private int vmCnt;
    private int vmUpCnt;

    private HostHwVo hostHwVo;
    private HostSwVo hostSwVo;

    private NicVo nicVo;


    // 리스트-> 상태, 이름(id), 코멘트, 호스트이름/ip, 클러스터(id), 데이터센터(id), 상태, 가상머신수, 메모리, cpu, 네트워크
    // 일반-> 호스트/ip, spm우선순위, 활성가상머신, 논리cpucore수, 온라인논리cpu코어수, 부팅시간, hostedEngineHA,
    //         iscsi 개시자이름, kdump, 물리적메모리, swap크기, 공유메모리, 장치통과
    //    하드웨어-> 생산자, 버전, cpu모델, 소켓당 cpu코어, 제품군, uuid, cpu유형, 코어당cpu스레드, 제품이름, 일련번호, cpu소켓, tsc주파수
    //    소프트웨어-> os버전, os정보, 커널버전, kvm 버전, libvirt버전, vdsm버전, glusterfs버전, ceph버전, open vswitch버전,
    //              nmstate버전, 머널기능, vnc 암호화, ovn configured,
    //              (db에서 값가져오기)

    // 가상머신-> 이름(id), 상태, 클러스터, ip주소, fqdn, 업타임, cpu,memory,network,
    // 네트워크 인터페이스-> 이름, mac, rx속도, 총rx, tx속도, 총tx속도, speed,
    //            논리네트워크: vlan, 네트워크 이름, ipv4, ipv6
    //            슬레이브: 이름, mac, 속도, rx속도, 총rx, tx속도, 총tx속도
    // 호스트장치-> 이름, 기능, 벤더, 제품, 드라이버, 현재사용중, 가상머신에연결됨, iommu그룹, mdev유형
    // 권한 -> 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, inherited from
    // 선호도 레이블-> 이름, 가상머신 멤버, 호스트 멤버


    // LINK: fenceagents, devices, hooks, numanodes, storage, katelloerrata, permissions, tags, cpuunits,
    //       nics, affinitylabels, networkattachments, storageconnectionextensions, unmanagednetworks, statistics

    // cluster
}
