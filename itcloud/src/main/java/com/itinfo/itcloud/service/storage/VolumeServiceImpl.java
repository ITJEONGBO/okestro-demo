package com.itinfo.itcloud.service.storage;

import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItVolumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VolumeServiceImpl implements ItVolumeService {
    @Autowired
    private AdminConnectionService adminConnectionService;

}
