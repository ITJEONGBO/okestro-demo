package com.itinfo.service.engine;

import java.util.Random;

import com.itinfo.model.ModelsKt;
import com.itinfo.model.SystemPropertiesVo;

import com.itinfo.service.SystemPropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.ConnectionBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
@Getter
@Component
@Slf4j
public class ConnectionService {
	@Autowired private SystemPropertiesService systemPropertiesService;
	private final String uid;

	public ConnectionService() {
		Random rnd = new Random();
		String randomStr
				= String.valueOf(rnd.nextInt(1000));
		this.uid = randomStr;
	}

	public Connection getConnection() {
		log.debug("getConnection ...");
		SystemPropertiesVo systemProperties = systemPropertiesService.retrieveSystemProperties();
		Connection connection = ModelsKt.toConnection(systemProperties);
		return connection;
	}

}
