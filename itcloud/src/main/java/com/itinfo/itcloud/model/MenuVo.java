package com.itinfo.itcloud.model;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.StorageDomainVo;
import com.itinfo.itcloud.model.storage.VolumeVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MenuVo {
    private List<DataCenterVo> datacenter;
    private List<ClusterVo> cluster;
    private List<HostVo> host;
    private List<VmVo> vm;
    private List<TemplateVo> tm;

    private List<VnicProfileVo> vnic;
    private List<NetworkVo> network;

    private List<StorageDomainVo> domain;
    private List<VolumeVo> volume;
    private List<DiskVo> disk;


}
