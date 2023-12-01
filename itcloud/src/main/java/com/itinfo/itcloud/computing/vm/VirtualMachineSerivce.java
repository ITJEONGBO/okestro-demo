package com.itinfo.itcloud.computing.vm;

import com.itinfo.itcloud.VO.DashBoardVO;
import com.itinfo.itcloud.VO.computing.VmNicVO;
import com.itinfo.itcloud.VO.computing.VmVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VirtualMachineSerivce {

    List<VmVO> showVmList();

//    List<VmVO> showList();

    List<VmNicVO> showVmNicList(String vmId);



}
