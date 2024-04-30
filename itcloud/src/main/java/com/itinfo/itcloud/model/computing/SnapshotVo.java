package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Getter @Setter @ToString @Builder
public class SnapshotVo {
    private String id;
    private String description;
    private String date;
    private boolean persistMemorystate; // 메모리 f/t
    private String status;
    private BigInteger setMemory;   // 설정된 메모리
    private BigInteger guaranteedMemory;    // 할당할 실제 메모리
    private int cpuCore;

    private List<SnapshotDiskVo> sDiskList;
    private List<NicVo> nicVoList;
    private List<ApplicationVo> appVoList;
}
