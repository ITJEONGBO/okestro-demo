package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VDiskVo {
    // 새 가상 디스크
//    https://ovirt.github.io/ovirt-engine-api-model/master/#create-virtual-machine-disk
    // 이미지
    private VDiskImageVo vDiskImageVo;

    // 직접 LUN
    private VDiskLunVo vDiskLunVo;

    // 관리되는 블록
    private VDiskBlockVo vDiskBlockVo;
}
