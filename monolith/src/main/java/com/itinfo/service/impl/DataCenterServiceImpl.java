package com.itinfo.service.impl;

import com.itinfo.model.ModelsKt;
import com.itinfo.service.DataCenterService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.DataCenterVo;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.DataCenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataCenterServiceImpl extends BaseService implements DataCenterService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private ConnectionService connectionService;


	@Override
	public List<DataCenterVo> retrieveDataCenters() {
		log.info("... retrieveDataCenters");
		Connection connection = connectionService.getConnection();
		List<DataCenter> dataCenters
				= getSysSrvHelper().findAllDataCenters(connection);
		return ModelsKt.toDataCenterVos(dataCenters, connection);
	}
}

