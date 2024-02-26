package com.itinfo.itcloud.model.enums;

public enum QuotaModeTypeVo {
    audit("감사"),
    disabled("비활성화됨"),
    enabled("강제적용");

    public final String name;
    QuotaModeTypeVo(String name) {
        this.name = name;
    }
}
