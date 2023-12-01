package com.itinfo.itcloud.storage.disk;

import com.itinfo.itcloud.VO.storage.DiskVO;
import com.itinfo.itcloud.ovirt.ConnectionService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.Disk;
import org.ovirt.engine.sdk4.types.DiskAttachment;
import org.ovirt.engine.sdk4.types.Vm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageStorageDiskServiceImpl implements StorageDiskSerivce {

//    @Autowired
//    private AdminConnectionService adminConnectionService;

    @Autowired
    private ConnectionService ovirtConnection;


    public StorageStorageDiskServiceImpl(){}


    @Override
    public List<DiskVO> showDiskList() {
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();

        List<Disk> diskList = ((DisksService.ListResponse)systemService.disksService().list().send()).disks();
        List<DiskVO> diskVoList = new ArrayList<>();

        diskList.forEach((disks)->{
            DiskVO diskVO = new DiskVO();
            Disk disk = ((DiskService.GetResponse)systemService.disksService().diskService(disks.id()).get().send()).disk();
            diskVO.setDiskId(disk.id());
            diskVO.setDiskName(disk.name());
            diskVO.setDescription(disk.description());

            diskVO.setStatus(disk.status().toString());
            diskVO.setFormat(disk.format().toString());

//            diskVO.setDiskProfileId(disk.diskProfile().id());

            diskVO.setActualSize(disk.actualSize().toString());
            diskVO.setVirtualSize(disk.provisionedSize().toString());   // 숫자 그거

            diskVO.setShareable(disk.shareable());
            diskVO.setStorageType(disk.storageType().name());

//            diskVO.setStorageDomainId(disk.storageDomain().id());
//            diskVO.setStorageDomainName(disk.storageDomain().name());
//            diskVO.setConnection(disk.);

            diskVoList.add(diskVO);
        });

        return diskVoList;
    }




}
