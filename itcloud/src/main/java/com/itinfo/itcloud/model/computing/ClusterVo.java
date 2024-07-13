package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString @Builder
public class ClusterVo {
    private String id;
    private String name;
    private String description;
    private String comment;
    private boolean ballooningEnabled;
    private String cpuType;     // cpu().type()
    private boolean threadsAsCore;
    private int memoryOverCommit;
    private String restoration;    // 복구정책
    private String chipsetFirmwareType;     // biosType
    private String version;
    private boolean gluster;
    private boolean virt;

    private String datacenterId;
    private String datacenterName;

    private int hostCnt;
    private int hostUpCnt;
    private int hostDownCnt;

    private int vmCnt;
    private int vmUpCnt;
    private int vmDownCnt;

    public void hostCnt(int hostCnt) {
        this.hostCnt = hostCnt;
    }
    public void hostUpCnt(int hostUpCnt) {
        this.hostUpCnt = hostUpCnt;
    }
    public void hostDownCnt(int hostDownCnt) {
        this.hostDownCnt = hostDownCnt;
    }
    public void vmCnt(int vmCnt) {
        this.vmCnt = vmCnt;
    }
    public void vmUpCnt(int vmUpCnt) {
        this.vmUpCnt = vmUpCnt;
    }
    public void vmDownCnt(int vmDownCnt) {
        this.vmDownCnt = vmDownCnt;
    }

    private List<HostVo> hostVoList;
}
