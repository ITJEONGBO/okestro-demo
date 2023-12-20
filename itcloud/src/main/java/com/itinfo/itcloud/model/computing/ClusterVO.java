package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.HostDetailVO;
import com.itinfo.itcloud.model.network.NetworkVO;
import com.itinfo.itcloud.model.UsageVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.AffinityGroup;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
public class ClusterVO {

    private String id;
    private String name;
    private String comment;
    private String version;
    private String description;

    private String cpuType;
    private String status;
    private String upgradeStatus;


    private String dataCenter;
    private boolean threadsAsCPU;
    private int memoryOverCommit;
    private boolean restoration;
    private String chipsetFirmwareType;


    private int hostCnt;
    private int hostUpCnt;
    private int hostDownCnt;

    private int vmCnt;
    private int vmUpCnt;
    private int vmDownCnt;

    private List<NetworkVO> networkVOList;
    private List<HostVO> hostVOList;
    private List<VmVO> vmVOList;
    private List<AffinityGroupVO> affinityGroupVOList;
    private List<CpuProfileVO> cpuProfileVOList;

}
