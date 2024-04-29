package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.storage.DiskVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class SnapshotVo {
    private String name;  // 이름이 description
    private String date;
    private boolean persistMemory;
    private String status;
//    private String type;

    private DiskVo diskVo;
    private NicVo nicVo;
    private ApplicationVo applicationVo;

}
