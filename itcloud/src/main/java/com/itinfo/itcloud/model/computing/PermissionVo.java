package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class PermissionVo {

    private String permissionId;

    private String datacenterId;

    // group
    private String groupId;
    private String groupName;   // user
    private String nameSpace;

    // role
    private String roleId;
    private String roleName;
    private String user;
    private String description;
    private boolean administrative;
    private boolean mutable;


    private String provider;
    private Date createDate;


    // Permission   -> data_center, group, role
    // group    -> name, domain_entry_id, namespace
    //          -> link(roles, permissions, tags)
    // role     -> name, description administrative, mutable
    //          -> link(roles.permits)


    // datacenter, group, role

    // 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, Inherited From
}
