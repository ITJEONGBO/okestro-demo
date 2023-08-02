package com.itinfo.service.engine;

import java.util.Random;

import com.itinfo.model.SystemPropertiesVo;

import com.itinfo.service.SystemPropertiesService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.ConnectionBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
@Getter
@Component
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
		SystemPropertiesVo systemProperties = this.systemPropertiesService.retrieveSystemProperties();
		String url = "https://" + systemProperties.getIp() + "/ovirt-engine/api";
		String user = systemProperties.getId() + "@internal";
		String password = systemProperties.getPassword();
		Connection connection = ConnectionBuilder.connection()
					.url(url)
					.user(user)
					.password(password)
					.insecure(true)
					.timeout(20000)
					.build();
		return connection;
	}

}
