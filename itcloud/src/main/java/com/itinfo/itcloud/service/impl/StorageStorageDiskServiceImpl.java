package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.storage.DiskVO;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.ovirt.ConnectionService;
import com.itinfo.itcloud.service.ItStorageDiskService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.Disk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageStorageDiskServiceImpl implements ItStorageDiskService {

    @Autowired
    private AdminConnectionService adminConnectionService;


}
