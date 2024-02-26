package com.itinfo.itcloud.model.enums;

public enum LogSeverityVo {
    alert("알림"),
    error("에러"),
    normal("보통"),
    warning("위험");

    private final String name;

    LogSeverityVo(String name) {
        this.name = name;
    }
}
