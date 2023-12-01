package com.itinfo.itcloud.VO;

import com.itinfo.itcloud.VO.computing.VmNicVO;
import com.itinfo.itcloud.VO.computing.VmSystemVO;
import com.itinfo.itcloud.VO.storage.DiskVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SnapshotVO {

    private String snapshotId;
    private String snapshotName;

    private String vmId;            // 가상머신 Id

    private long date;              // 날짜
    private String status;          // 상태
    private boolean memory;         // 메모리 false
    private String description;     // 설명
    private VmSystemVO vmSystem;    // 설정된메모리, cpu 코어수

    private List<DiskVO> disks;     // 디스크 정보


    private boolean memoryRestore;

    private List<VmNicVO> nics;
}
