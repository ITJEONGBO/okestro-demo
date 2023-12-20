package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AffinityGroupVO {
    private String id;
    private String name;
    private String status;
    private String description;
    private int priority;  // 우선순위

    private String positive;
    private String enforcing;

    // 가상머신 측 극성, 가상머신 강제적용
    private boolean vmEnabled;
    private boolean vmPositive;
    private boolean vmEnforcing;

    // 호스트 측 극성, 호스트 강제적용
    private boolean hostEnabled;
    private boolean hostPositive;
    private boolean hostEnforcing;


    // 가상머신 레이블, 호스트 레이블
    private String vmLabels;
    private String hostLabels;

    // 가상머신 멤버, 호스트 멤버
    private String vmList;
    private String hostList;
//    private List<VmVO> vmVOList;
//    private List<HostVO> hostVOList;



}
