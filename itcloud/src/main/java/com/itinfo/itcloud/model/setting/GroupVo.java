package com.itinfo.itcloud.model.setting;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupVo {
    private String id;
    private String name;
    private String nameSpace;

    private RoleVo roleVo;

    // link - roles, permissions, tags
}
