package com.itinfo.itcloud.ovirt;

import com.itinfo.itcloud.admin.property.SystemPropertiesService;
import com.itinfo.itcloud.VO.SystemPropertiesVO;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.ConnectionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ConnectionService {

    @Autowired
    private SystemPropertiesService systemPropertiesService;

//    private final String uid;

    public ConnectionService() {
        /*Random rnd = new Random();
        String randomStr = String.valueOf(rnd.nextInt(1000));
        this.uid = randomStr;*/
    }

    public Connection getConnection(){
        SystemPropertiesVO systemPropertiesVO = this.systemPropertiesService.searchSystemProperties();
//        System.out.println(systemPropertiesVO.toString());
        String url = "https://" + systemPropertiesVO.getIp() + "/ovirt-engine/api";
        String id = systemPropertiesVO.getId() + "@internal";
        String pw = systemPropertiesVO.getPassword();
        Connection connection = ConnectionBuilder.connection().url(url).user(id).password(pw).insecure(true).timeout(20000).build();
        return connection;
    }

    /*public String getUid() {
        return this.uid;
    }*/

}
