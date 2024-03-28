package com.itinfo.itcloud.model.storage;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class ImageVo {
    private int size;   // 크기(Gib)
    private String alias;
    private String description;
    private DataCenterVo dcVo;
    private DomainVo domainVo;
    private boolean sp;
    private DiskProfileVo diskProfileVo;

    private boolean wipeAfterDelete; // 삭제 후 초기화
    private boolean shareable;  // 공유가능
    private String backup;  // 증분 백업 사용
}
