package com.itinfo.itcloud.dao.impl;

import com.itinfo.itcloud.dao.VirtualMachineDAO;
import com.itinfo.itcloud.model.VmNetworkUsageVO;
import com.itinfo.itcloud.model.VmUsageVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VirtualMachineDAOImpl implements VirtualMachineDAO {

    @Autowired
    private SqlSession SqlSession;


    @Override
    public VmUsageVO retrieveVmUsageOne(String id) {
        return (VmUsageVO) this.SqlSession.selectOne("Virtualmachine.retrieveVmUsageOne", id);
    }

    @Override
    public VmNetworkUsageVO retrieveVmNetworkUsageOne(List<String> nicIds) {
        return (VmNetworkUsageVO)this.SqlSession.selectOne("Virtualmachine.retrieveVmNetworkUsageOne", nicIds);
    }
}
