package com.itinfo.itcloud.dao;

import com.itinfo.itcloud.model.VmNetworkUsageVO;
import com.itinfo.itcloud.model.VmUsageVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirtualMachineDAO {
    VmUsageVO retrieveVmUsageOne(String id);

    VmNetworkUsageVO retrieveVmNetworkUsageOne(List<String> nicIds);
}
