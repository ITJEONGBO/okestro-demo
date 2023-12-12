package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.dao.SystemPropertiesDAO;
import com.itinfo.itcloud.model.SystemPropertiesVO;
import com.itinfo.itcloud.service.SystemPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemPropertiesServiceImpl implements SystemPropertiesService {

    @Autowired
    private SystemPropertiesDAO systemPropertiesDAO;

    public SystemPropertiesServiceImpl(){}

    @Override
    public SystemPropertiesVO searchSystemProperties() {
        return this.systemPropertiesDAO.searchSystemProperties();
    }

    @Override
    public int saveSystemProperties(SystemPropertiesVO systemPropertiesVO) {
        return this.systemPropertiesDAO.updateSystemProperties(systemPropertiesVO);
    }


}