package com.itinfo.itcloud.repository.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString @Builder
public class UsageDto {
    // host, vm 상세 그래프
    private String id;
    private String name;

    private int cpuPercent;
    private int memoryPercent;
    private int networkPercent;
}
