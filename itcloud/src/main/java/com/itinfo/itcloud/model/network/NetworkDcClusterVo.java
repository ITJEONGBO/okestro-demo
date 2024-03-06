package com.itinfo.itcloud.model.network;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class NetworkDcClusterVo {
    private DataCenterVo dataCenterVo;
    private List<ClusterVo> clusterVoList;
}
