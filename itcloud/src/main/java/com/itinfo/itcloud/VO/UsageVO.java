package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UsageVO {
    // 그래프에 필요한 용량을 알려주는
    
    private Integer cpuUsages;
    private Integer memoryUsages;
    private Integer networkUsages;
    private Integer storageUsages;
    private Integer transitUsages;
    private Integer receiveUsages;
    private String usageDate;
    private String StorageUsageDate;

}
