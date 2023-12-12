package com.itinfo.itcloud.dao;

import com.itinfo.itcloud.model.SystemPropertiesVO;

public interface SystemPropertiesDAO {
    SystemPropertiesVO searchSystemProperties();

    int updateSystemProperties(SystemPropertiesVO systemProperties);
}