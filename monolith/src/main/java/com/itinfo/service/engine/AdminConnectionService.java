package com.itinfo.service.engine;

import com.itinfo.service.SystemPropertiesService;

import com.itinfo.model.SystemPropertiesVo;

import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.ConnectionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
public class AdminConnectionService {
	@Autowired private SystemPropertiesService systemPropertiesService;

	public Connection getConnection() {
		Connection connection = null;
		SystemPropertiesVo systemProperties = this.systemPropertiesService.retrieveSystemProperties();
		try {
			String url = "https://" + systemProperties.getIp() + "/ovirt-engine/api";
			String user = systemProperties.getId() + "@internal";
			String password = systemProperties.getPassword();
			connection = ConnectionBuilder.connection().url(url).user(user).password(password).insecure(true).build();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage() + "adminConnectionERROR");
			e.printStackTrace();
		}
		return connection;
	}
}
