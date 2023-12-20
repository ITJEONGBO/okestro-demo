package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AffinityLabelVO {
    private String id;
    private String name;

    private List<String> hostsLabel;    // 가상머신 멤버
    private List<String> vmsLabel;      // 호스트 멤버
}
