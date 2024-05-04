package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.IdentifiedVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class VmSetVo {
    private String clusterId;
    private String clusterName;

    private String dcName;

    // 인스턴스 이미지 연결, 생성

    private List<VmVnicVo> vnicList;
    private List<IdentifiedVo> hostVoList;
    private List<IdentifiedVo> profileVoList;

    private List<IdentifiedVo> agVoList;
    private List<IdentifiedVo> alVoList;


//    private List<NicVo> nicVoList;
//    private List<HostVo> hostVoList;
//    private List<CpuProfileVo> profileVoList;
//    private List<DiskVo> diskVoList;
//
//    private List<AffinityGroupVo> agVoList;
//    private List<AffinityLabelVo> alVoList;
}
