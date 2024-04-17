package com.itinfo.itcloud.service.impl;

import com.itinfo.util.BasicConfiguration;
import com.itinfo.util.model.SystemPropertiesVo;
import com.itinfo.itcloud.service.ItSystemPropertyService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemPropertyServiceImpl implements ItSystemPropertyService {

//	private final SystemPropertiesDAO systemPropertiesDAO;
	private BasicConfiguration getBasicConf() {
		return BasicConfiguration.getInstance();
	}

	@Override
	public SystemPropertiesVo searchSystemProperties() {
		return getBasicConf().getSystemProperties();
	}

	@Override
	public int saveSystemProperties(SystemPropertiesVo systemPropertiesVO) {
		return 1;
	}
}