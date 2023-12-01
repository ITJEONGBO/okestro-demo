package com.itinfo.itcloud.admin.property;

import com.itinfo.itcloud.VO.SystemPropertiesVO;

public interface SystemPropertiesDAO {
    SystemPropertiesVO searchSystemProperties();

    int updateSystemProperties(SystemPropertiesVO systemProperties);
}