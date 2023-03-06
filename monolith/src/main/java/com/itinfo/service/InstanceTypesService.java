package com.itinfo.service;

import com.itinfo.model.InstanceTypeVo;

import java.util.List;

public interface InstanceTypesService {
	List<InstanceTypeVo> retrieveInstanceTypes();

	InstanceTypeVo retrieveInstanceTypeCreateInfo();

	String createInstanceType(InstanceTypeVo instanceType);

	InstanceTypeVo retrieveInstanceTypeUpdateInfo(String id);

	String updateInstanceType(InstanceTypeVo instanceType);

	String removeInstanceType(InstanceTypeVo instanceType);
}
