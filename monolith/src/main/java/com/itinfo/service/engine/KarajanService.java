package com.itinfo.service.engine;

import com.itinfo.model.karajan.*;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.model.SystemPropertiesVo;

import java.util.List;

import com.itinfo.service.impl.BaseService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.ClustersService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.Cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KarajanService extends BaseService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private SystemPropertiesService systemPropertiesService;

	public KarajanVo getDataCenter() {
		Connection connection = adminConnectionService.getConnection();
		SystemPropertiesVo properties
				= systemPropertiesService.retrieveSystemProperties();
		return KarajanModelsKt.toKarajanVo(properties, connection);
	}
}
