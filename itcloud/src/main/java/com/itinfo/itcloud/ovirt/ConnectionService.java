package com.itinfo.itcloud.ovirt;

import com.itinfo.itcloud.service.ItSystemPropertyService;
import com.itinfo.itcloud.model.SystemPropertiesVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class ConnectionService {

    @Autowired
    private ItSystemPropertyService itSystemPropertyService;

    @Getter
    private final String uid;

    public ConnectionService() {
        Random rnd = new Random();
        String randomStr = String.valueOf(rnd.nextInt(1000));
        this.uid = randomStr;
    }

    public Connection getConnection(){
        SystemPropertiesVO systemPropertiesVO = itSystemPropertyService.searchSystemProperties();
        log.info(systemPropertiesVO.getIp());
        return systemPropertiesVO.getConnection();
    }

}
