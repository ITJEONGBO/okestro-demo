package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.DataCenterVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItDataCenterService {

    List<DataCenterVO> getDatacenters();
    DataCenterVO getStorage(String id);
    DataCenterVO getNetwork(String id);
    DataCenterVO getCluster(String id);

}
