package com.itinfo.itcloud.ovirt;

import com.itinfo.itcloud.VO.SystemPropertiesVO;
import com.itinfo.itcloud.admin.property.SystemPropertiesService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.ConnectionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminConnectionService {

    @Autowired
    private SystemPropertiesService systemPropertiesService;

    public AdminConnectionService(){}

    public Connection getConnection(){
        Connection connection = null;
        SystemPropertiesVO systemPropertiesVO = this.systemPropertiesService.searchSystemProperties();

        try {
            String url = "https://" + systemPropertiesVO.getIp() + "/ovirt-engine/api";
            String user = systemPropertiesVO.getId() + "@internal";
            String password = systemPropertiesVO.getPassword();
            connection = ConnectionBuilder.connection().url(url).user(user).password(password).insecure(true).build();
        } catch (Exception var6) {
            System.out.println("adminConnectionERROR");
            var6.printStackTrace();
        }

        return connection;
    }

}
