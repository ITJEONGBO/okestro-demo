package com.itinfo.service.engine;

import com.itinfo.model.karajan.*;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.model.SystemPropertiesVo;

import java.util.ArrayList;
import java.util.List;

import com.itinfo.service.impl.BaseService;
import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.Vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WorkloadPredictionService extends BaseService {
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	private final String queryGetVmWorkload;
	public WorkloadPredictionService(String queryGetVmWorkload) {
		this.queryGetVmWorkload = queryGetVmWorkload;
	}

	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private SystemPropertiesService systemPropertiesService;


	@Scheduled(cron = "0 0 01 * * ?")
	public void makeLearning() {
		SystemPropertiesVo properties = systemPropertiesService.retrieveSystemProperties();
		if (properties.getDeepLearningUri().length() > 0) {
			log.info("make learning " + properties.getDeepLearningUri());
			WorkloadVo workload = getWorkload();
			RestTemplate rest = new RestTemplate();
			String result
					= (String)rest.postForObject(properties.getDeepLearningUri(), workload, String.class, new Object[0]);
			log.info("result: {}" + result);
		}
	}

	public WorkloadVo getWorkload() {
		Connection connection = this.adminConnectionService.getConnection();
		SystemPropertiesVo properties = systemPropertiesService.retrieveSystemProperties();
		WorkloadVo workload
				= KarajanModelsKt.toWorkloadVo(properties, connection);
		return workload;
	}

	private List<WorkloadVmVo> getVms(Connection connection, String clusterName) {
		List<WorkloadVmVo> targets = new ArrayList<>();
		List<Vm> vms
				= getSysSrvHelper().findAllVms(connection, "cluster=" + clusterName);
		vms.forEach(vm -> {
			WorkloadVmVo target
					= KarajanModelsKt.toWorkloadVmVo(vm, connection, jdbcTemplate);
			List<HistoryVo> histories
					= jdbcTemplate.query(this.queryGetVmWorkload,
						new String[] {
							String.valueOf((target.getMemoryInstalled() != null)
									? target.getMemoryInstalled()
									: Integer.valueOf(0)), vm.id()
						}, new BeanPropertyRowMapper(HistoryVo.class));
			target.setHistories(histories);
			targets.add(target);
		});
		return targets;
	}



}
