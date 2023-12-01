package com.itinfo.itcloud.VO.computing;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TemplateVO {
    private String orgVmId;
    private String id;
    private String name;
    private String versionName;
    private String version;
    private String description;
    private long creationTime;
    private String status;
    private boolean forceOverride;
    private String os;
    private VmSystemVO systemInfo;
    private List<VmVO> vms;
    private List<VmNicVO> nics;
//    private List<EventVo> events;
//    private ClusterVo cluster;
    private String cpuProfileId;
    private String quotaId;
//    private List<TemplateDiskVo> templateDisks;
    private String rootTemplateId;
    private List<TemplateVO> rootTemplates;
    private String subVersionName;
    private boolean allUserAccess;
    private boolean clonePermissions;
    private boolean seal;
    private int diskAttachmentSize;
}
