package com.itinfo.itcloud.model.create;

import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.VmVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class AffinityLabelCreateVo {
    private String id;
    private String name;

//    private List<String> hostList; //id
//    private List<String> vmList;

    private List<HostVo> hostList;
    private List<VmVo> vmList;

}
