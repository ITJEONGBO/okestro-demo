package com.itinfo.itcloud.model.enums;

public enum DataCenterStatusVo {
    contend("contend"),
    maintenance("maintenance"),
    not_operational("not_operational"),
    problematic("problematic"),
    uninitialized("초기화되지 않음"),
    up("Up");

    public final String name;

    DataCenterStatusVo(String name) {
        this.name = name;
    }
}
