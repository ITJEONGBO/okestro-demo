package com.itinfo.itcloud.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class UsageChartDto {
    private String name;
    private int percent;
}
