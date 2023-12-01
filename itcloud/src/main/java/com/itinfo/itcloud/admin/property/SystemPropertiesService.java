package com.itinfo.itcloud.admin.property;

import com.itinfo.itcloud.VO.SystemPropertiesVO;

public interface SystemPropertiesService {
    SystemPropertiesVO searchSystemProperties();

    int saveSystemProperties(SystemPropertiesVO systemPropertiesVO);
}