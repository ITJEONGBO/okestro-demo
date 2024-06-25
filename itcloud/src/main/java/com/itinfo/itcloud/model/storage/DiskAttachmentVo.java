package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class DiskAttachmentVo {
    private boolean active;
    private boolean bootable;
    private boolean readOnly;
    private String interface_;
    private String logicalName;
    private DiskVo diskVo;
}
