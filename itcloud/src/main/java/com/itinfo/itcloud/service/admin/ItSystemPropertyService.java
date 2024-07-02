package com.itinfo.itcloud.service.admin;

import com.itinfo.util.model.SystemPropertiesVo;

public interface ItSystemPropertyService {
    SystemPropertiesVo searchSystemProperties();

    int saveSystemProperties(SystemPropertiesVo systemPropertiesVO);
}