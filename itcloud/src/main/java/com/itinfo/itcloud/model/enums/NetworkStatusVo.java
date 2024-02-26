package com.itinfo.itcloud.model.enums;

public enum NetworkStatusVo {
    non_operational("비가동 중"),
    operational("가동 중");

    public final String name;

    NetworkStatusVo(String name) {
        this.name = name;
    }
}
