package com.itinfo.itcloud.repository.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class UsageDto {
    // host, vm 상세 그래프
    private String id;
    private String name;

    private Integer cpuPercent;
    private Integer memoryPercent;
    private Integer networkPercent;
}
