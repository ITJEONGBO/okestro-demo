package com.itinfo.service.impl;

import com.itinfo.SystemServiceHelper;
import com.itinfo.model.ModelsKt;
import com.itinfo.service.DataCenterService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.DataCenterVo;

import java.util.List;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.DataCenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataCenterServiceImpl implements DataCenterService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private ConnectionService connectionService;

	public List<DataCenterVo> retrieveDataCenters() {
		Connection connection = this.connectionService.getConnection();

		List<DataCenter> dataCenters
				= SystemServiceHelper.getInstance().findAllDataCenters(connection);
		List<DataCenterVo> dataCentersInfo
				= ModelsKt.toDataCenterVos(dataCenters);
		return dataCentersInfo;
	}
}

