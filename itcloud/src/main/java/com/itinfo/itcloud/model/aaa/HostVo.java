package com.itinfo.itcloud.model.aaa;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HostVo {
    private String id;
    private String name;
    private String comment;
    private String address;
    private String cpuName;
    private String cpuType;
    private String cpuTopologyCore;
    private String cpuTopologySocket;
    private String cpuTopologyThread;
    private String devicePassThrough;




    // 리스트-> 상태, 이름(id), 코멘트, 호스트이름/ip, 클러스터(id), 데이터센터(id), 상태, 가상머신수, 메모리, cpu, 네트워크
    // 일반-> 호스트/ip, spm우선순위, 활성가상머신, 논리cpucore수, 온라인논리cpu코어수, 부팅시간, hostedEngineHA,
    //         iscsi 개시자이름, kdump, 물리적메모리, swap크기, 공유메모리, 장치통과
    //    하드웨어
    // 가상머신->
    // 네트워크 인터페이스->
    // 호스트장치->
    // 권한 -> 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, inherited from
    // 선호도 레이블->


    // LINK: fenceagents, devices, hooks, numanodes, storage, katelloerrata, permissions, tags, cpuunits,
    //       nics, affinitylabels, networkattachments, storageconnectionextensions, unmanagednetworks, statistics
    //
}
