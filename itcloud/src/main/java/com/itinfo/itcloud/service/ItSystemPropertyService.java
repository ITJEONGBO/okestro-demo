package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.SystemPropertiesVO;

public interface ItSystemPropertyService {
    SystemPropertiesVO searchSystemProperties();

    int saveSystemProperties(SystemPropertiesVO systemPropertiesVO);
}