package com.itinfo.itcloud.storage.disk;

import com.itinfo.itcloud.VO.storage.DiskVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StorageDiskSerivce {

    List<DiskVO> showDiskList();

//    List<DiskVO> showDisk(String vmId);
}
