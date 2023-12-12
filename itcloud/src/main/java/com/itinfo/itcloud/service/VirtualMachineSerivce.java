package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.VmNicVO;
import com.itinfo.itcloud.model.computing.VmVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VirtualMachineSerivce {

    List<VmVO> showVmList();

//    List<VmVO> showList();

    List<VmNicVO> showVmNicList(String vmId);



}
