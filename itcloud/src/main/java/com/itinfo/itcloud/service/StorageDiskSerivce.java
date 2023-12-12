package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.storage.DiskVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StorageDiskSerivce {

    List<DiskVO> showDiskList();

//    List<DiskVO> showDisk(String vmId);
}
