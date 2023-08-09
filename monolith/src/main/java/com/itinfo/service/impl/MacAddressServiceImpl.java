package com.itinfo.service.impl;

import com.itinfo.model.ModelsKt;
import com.itinfo.service.MacAddressService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.MacAddressPoolsVo;

import java.util.List;

import lombok.NoArgsConstructor;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.MacPool;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
@NoArgsConstructor
public class MacAddressServiceImpl extends BaseService implements MacAddressService {

	@Autowired private ConnectionService connectionService;
	@Override
	public List<MacAddressPoolsVo> retrieveMacAddressPools() {
		log.info("... retrieveMacAddressPools");
		Connection connection = connectionService.getConnection();
		List<MacPool> macPoolList
				= getSysSrvHelper().findAllMacPools(connection);
		return ModelsKt.toMacAddressPoolsVos(macPoolList);
	}
}
