package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.storage.DiskVO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VmDiskVO {
    private String diskId;


    // disks
    private boolean acitve;
    private boolean bootable;   // 부팅가능
    private boolean passDiscard;   // 공유가능
    private boolean readOnly;   // 읽기전용

    // 연결대상

    private String interfaceName;
    private String logicalName;

    private DiskVO diskVO;
}
