package com.itinfo.itcloud.model.aaa.computing;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class HostVo {
    private String id;
    private String name;
    private String comment;
    private String address;
    private String status;
    private int cpuTopologyCore;
    private int cpuTopologySocket;
    private int cpuTopologyThread;
    private boolean devicePassThrough;
    private String iscsi;
    private String kdump;
    private String bootingTime;
    private BigInteger maxMemory; // 새 가상머신 최대여유메모리
    private BigInteger memory;
    private BigInteger memoryUsed;
    private BigInteger memoryFree;
    private String seLinux;
    private int spmPriority;

    // Hosted Engine HA
    // Swap 크기
    // 공유 메모리
    // 메모리 페이지 공유
    // 자동으로 페이지를 크게
    // Huge Pages (size: free/total)
    // 클러스터 호환 버전


    // 하드웨어
    private String hwFamily;
    private String hwManufacturer;
    private String hwProduct;
    private String hwSerialNum;
    private String hwUuid;
    private String hwVersion;
    private String cpuName;
    private String cpuType;
    // cpu소켓
    // TSC 주파수

    // 소프트웨어
    private String osVersion;
    private String osType;  //?
    private String libvirtVersion;
    private String vdsmVersion;
    // 커널,kvm,spice,glusterfs,ceph,openvswitch,nmstate,커널기능,vnc암호화,ovn configured


    private String clusterId;
    private String clusterName;
    private String datacenterId;
    private String datacenterName;

    private int vmCnt;
    private int vmUpCnt;


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
