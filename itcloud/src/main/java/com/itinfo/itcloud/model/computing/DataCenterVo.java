package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.network.NetworkVo;
import lombok.*;
import java.util.List;

@Getter @ToString @Builder
public class DataCenterVo {
    private String id;
    private String name;
    private String comment;
    private String description;
    private String quotaMode;     // QuotaMode: 비활성화됨, 감사, 강제적용
    private boolean storageType;         // local
    private String version;
    private String status;

    private List<NetworkVo> networkList;
}
