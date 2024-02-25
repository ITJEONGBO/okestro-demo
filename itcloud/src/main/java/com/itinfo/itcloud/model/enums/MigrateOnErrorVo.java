package com.itinfo.itcloud.model.enums;

public enum MigrateOnErrorVo {
    do_not_migrate("아니요"),
    migrate("예"),
    migrate_highly_available("높은 우선 순위만");

    public final String s;
    MigrateOnErrorVo(String s) {
        this.s = s;
    }
}
