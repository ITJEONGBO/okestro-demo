package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VDiskVo {
    private VDiskImageVo vDiskImageVo;  // 이미지
    private VDiskLunVo vDiskLunVo;      // 직접 LUN

//    private VDiskBlockVo vDiskBlockVo;  // 관리되는 블록
}
