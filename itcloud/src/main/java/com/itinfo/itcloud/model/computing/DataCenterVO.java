package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.network.NetworkVO;
import com.itinfo.itcloud.model.storage.DomainVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class DataCenterVO {
    private String id;
    private String name;
    private String comment;
    private String description;

    private boolean storageType;    //local 공유
    private String status;
    private String version;

    List<DomainVO> domainVOList;
    List<NetworkVO> networkVOList;
    List<ClusterVO> clusterVOList;
    //qos

}
