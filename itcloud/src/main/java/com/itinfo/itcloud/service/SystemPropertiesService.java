package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.SystemPropertiesVO;

public interface SystemPropertiesService {
    SystemPropertiesVO searchSystemProperties();

    int saveSystemProperties(SystemPropertiesVO systemPropertiesVO);
}