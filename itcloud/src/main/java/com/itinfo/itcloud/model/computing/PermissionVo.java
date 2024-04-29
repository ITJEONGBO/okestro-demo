package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter @ToString @Builder
public class PermissionVo {
    private String permissionId;      // permission
    private String datacenterName;
    private String clusterName;

    // group, user
    private String user;        // 사용자
    private String nameSpace;
    private String role;

    private Date createDate;
    private String provider;
    private String inheritedFrom;

    //    private List<GroupVo> groupVoList;
    //    private List<UserVo> userVoList;


    // 사용자, 인증 공급자, 네임스페이스, 역할, 생성일, Inherited From

    // Permission   -> data_center, group, role
    // group    -> name, domain_entry_id, namespace
    //          -> link(roles, permissions, tags)
    // role     -> name, description administrative, mutable
    //          -> link(roles.permits)


    // datacenter, group, role


}
