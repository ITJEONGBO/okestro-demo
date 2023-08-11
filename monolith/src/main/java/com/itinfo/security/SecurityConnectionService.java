package com.itinfo.security;

import com.itinfo.model.ModelsKt;
import com.itinfo.service.SystemPropertiesService;

import com.itinfo.model.SystemPropertiesVo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Getter
@Setter
public class SecurityConnectionService {
	@Autowired private SystemPropertiesService systemPropertiesService;
	private String url;
	private String user;
	private String password;

	public Connection getConnection() {
		log.info("... getConnection");
		SystemPropertiesVo systemProperties = systemPropertiesService.retrieveSystemProperties();
		Connection connection
				= ModelsKt.toConnection(systemProperties);
		return connection;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return this.url;
	}
}

