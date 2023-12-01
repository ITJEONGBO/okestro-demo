package com.itinfo.itcloud.computing.vm;

import com.itinfo.itcloud.VO.VmNetworkUsageVO;
import com.itinfo.itcloud.VO.VmUsageVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirtualMachineDAO {
    VmUsageVO retrieveVmUsageOne(String id);

    VmNetworkUsageVO retrieveVmNetworkUsageOne(List<String> nicIds);
}
