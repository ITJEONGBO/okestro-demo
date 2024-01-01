package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.dao.StorageDomainDAO;
import com.itinfo.itcloud.service.ItStorageDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageItStorageDomainServiceImpl implements ItStorageDomainService {
    @Autowired
    private AdminConnectionService adminConnectionService;


}
