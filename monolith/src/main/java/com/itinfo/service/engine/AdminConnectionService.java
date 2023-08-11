package com.itinfo.service.engine;

import com.itinfo.model.ModelsKt;
import com.itinfo.service.SystemPropertiesService;

import com.itinfo.model.SystemPropertiesVo;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminConnectionService {
	@Autowired private SystemPropertiesService systemPropertiesService;

	public Connection getConnection() {
		log.debug("getConnection ... ");
		Connection connection = null;
		SystemPropertiesVo systemProperties = this.systemPropertiesService.retrieveSystemProperties();
		try {
			connection = ModelsKt.toConnection(systemProperties);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage() + "adminConnectionERROR");
		}
		return connection;
	}
}
