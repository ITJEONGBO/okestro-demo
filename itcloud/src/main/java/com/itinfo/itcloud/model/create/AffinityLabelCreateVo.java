package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class AffinityLabelCreateVo {
    private String id;
    private String name;

    private List<String> hostList; //id
    private List<String> vmList;
}
