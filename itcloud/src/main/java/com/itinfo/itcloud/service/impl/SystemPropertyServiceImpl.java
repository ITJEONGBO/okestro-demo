package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.dao.SystemPropertiesDAO;
import com.itinfo.itcloud.model.SystemPropertiesVO;
import com.itinfo.itcloud.service.ItSystemPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemPropertyServiceImpl implements ItSystemPropertyService {

    @Autowired
    private SystemPropertiesDAO systemPropertiesDAO;

    public SystemPropertyServiceImpl(){}

    @Override
    public SystemPropertiesVO searchSystemProperties() {
        return this.systemPropertiesDAO.searchSystemProperties();
    }

    @Override
    public int saveSystemProperties(SystemPropertiesVO systemPropertiesVO) {
        return this.systemPropertiesDAO.updateSystemProperties(systemPropertiesVO);
    }


}