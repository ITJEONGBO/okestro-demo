package com.itinfo.service.engine;

import com.itinfo.model.karajan.*;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.model.SystemPropertiesVo;
import com.itinfo.service.impl.BaseService;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KarajanService extends BaseService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private SystemPropertiesService systemPropertiesService;

	public KarajanVo getDataCenter() {
		log.debug("getDataCenter ...");
		Connection connection = adminConnectionService.getConnection();
		SystemPropertiesVo properties
				= systemPropertiesService.retrieveSystemProperties();
		return KarajanModelsKt.toKarajanVo(properties, connection);
	}
}
