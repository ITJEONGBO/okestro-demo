package com.itinfo.service.impl;

import com.itinfo.model.*;
import com.itinfo.service.InstanceTypesService;

import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.service.engine.WebsocketService;

import com.google.gson.Gson;

import java.util.List;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.Builders;
import org.ovirt.engine.sdk4.types.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InstanceTypesServiceImpl extends BaseService implements InstanceTypesService {
	@Autowired private ConnectionService connectionService;
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private WebsocketService websocketService;

	@Override
	public List<InstanceTypeVo> retrieveInstanceTypes() {
		log.info("retrieveInstanceTypes ...");
		Connection connection = connectionService.getConnection();
		List<InstanceType> instanceTypeList
				= getSysSrvHelper().findAllInstanceTypes(connection);

		return ModelsKt.toInstanceTypeVos(instanceTypeList, connection);
	}

	@Override
	public InstanceTypeVo retrieveInstanceTypeCreateInfo() {
		log.info("retrieveInstanceTypeCreateInfo ...");
		Connection connection = connectionService.getConnection();
		List<VnicProfile> nicItemList
				= getSysSrvHelper().findAllVnicProfiles(connection);
		List<VmNicVo> vnics
				= ModelsKt.toVmNicVos(nicItemList);

		InstanceTypeVo instanceType = new InstanceTypeVo();
		instanceType.setNics(vnics);
		instanceType.setAffinity("migratable");
		return instanceType;
	}

	@Async("karajanTaskExecutor")
	@Override
	public String createInstanceType(InstanceTypeVo instanceType) {
		log.info("createInstanceType ... ");
		Connection connection = adminConnectionService.getConnection();
		InstanceType response = null;
		Gson gson = new Gson();
		try {
			InstanceType it
					= ModelsKt.toInstanceType(instanceType);
			response
					= getSysSrvHelper().addInstanceType(connection, it);
			if (instanceType.getSelectNics().size() > 0) {
				VmNicVo vmNic = instanceType.getSelectNics().get(0);
				Nic nic = Builders.nic()
						.name(vmNic.getNicName())
						.vnicProfile(Builders.vnicProfile().id(vmNic.getId())).build();
				Boolean res
					= getSysSrvHelper().addNicForInstanceType(connection, response.id(), nic);
			}
			Thread.sleep(3000L);
			MessageVo message
					= MessageVo.createMessage(MessageType.INSTANCE_TYPE_ADD, true, response.name(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			MessageVo message
					= MessageVo.createMessage(MessageType.INSTANCE_TYPE_ADD, false, e.getMessage(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
		return response.id();
	}

	@Override
	public InstanceTypeVo retrieveInstanceTypeUpdateInfo(String id) {
		log.info("retrieveInstanceTypeUpdateInfo('{}') ...", id);
		Connection connection = connectionService.getConnection();
		InstanceType item
				= getSysSrvHelper().findInstanceType(connection, id);
		InstanceTypeVo instanceType
				= ModelsKt.toInstanceTypeVo(item, connection);
		return instanceType;
	}

	@Async("karajanTaskExecutor")
	@Override
	public String updateInstanceType(InstanceTypeVo instanceType) {
		log.info("updateInstanceType ...");
		Connection connection = this.adminConnectionService.getConnection();
		InstanceType response = null;
		Gson gson = new Gson();
		try {
			InstanceType it
					= ModelsKt.toInstanceType(instanceType);
			response
					= getSysSrvHelper().updateInstanceType(connection, it);
			Thread.sleep(3000L);
			MessageVo message
					= MessageVo.createMessage(MessageType.INSTANCE_TYPE_UPDATE, true, response.name(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			MessageVo message
					= MessageVo.createMessage(MessageType.INSTANCE_TYPE_UPDATE, false, e.getMessage(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
		return response.id();
	}

	@Override
	public String removeInstanceType(InstanceTypeVo instanceType) {
		log.info("removeInstanceType ...");
		Connection connection = connectionService.getConnection();
		Boolean res
				= getSysSrvHelper().removeInstanceType(connection, instanceType.getId());
		String result = instanceType.getName() + " 삭제 " + (res ? "완료" : "실패");
		return result;
	}
}
