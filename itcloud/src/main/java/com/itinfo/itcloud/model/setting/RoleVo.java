package com.itinfo.itcloud.model.setting;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoleVo {
    private String id;
    private String name;
    private String description;
    private boolean administrative;
    private boolean mutable;

    // link - permits
}
