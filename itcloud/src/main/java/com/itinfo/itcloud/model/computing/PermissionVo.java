package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter @ToString @Builder
public class PermissionVo {
    private String permissionId;      // permission
//    private String datacenterName;
//    private String clusterName;

    // group, user
    private String user;        // 사용자
    private String nameSpace;
    private String role;

    private Date createDate;
    private String provider;
    private String inheritedFrom;

}
