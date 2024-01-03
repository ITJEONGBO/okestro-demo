package com.itinfo.itcloud.model.setting;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserVo {
    private String id;
    private String name;
    private String department;
    private String email;
    private String lastName;
    private String nameSpace;
    private String principal;
    private String userName;
    private String provider;    // 공급자?

    private RoleVo roleVo;

    // link - groups, roles, permissions, tags, sshpublickeys, eventsubscriptions, options
    // domain
}
