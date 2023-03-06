package com.itinfo.service;

import com.itinfo.model.SystemPropertiesVo;

public interface SystemPropertiesService {
	SystemPropertiesVo retrieveSystemProperties();
	Object[] retrieveProgramVersion();
	Integer saveSystemProperties(SystemPropertiesVo paramSystemPropertiesVo);
}
