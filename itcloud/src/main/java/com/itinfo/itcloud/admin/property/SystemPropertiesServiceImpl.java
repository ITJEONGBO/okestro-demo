package com.itinfo.itcloud.admin.property;

import com.itinfo.itcloud.VO.SystemPropertiesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemPropertiesServiceImpl implements SystemPropertiesService{

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