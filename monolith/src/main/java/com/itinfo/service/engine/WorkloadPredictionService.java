package com.itinfo.service.engine;

import com.itinfo.model.karajan.*;
import com.itinfo.model.SystemPropertiesVo;
import com.itinfo.service.impl.BaseService;
import com.itinfo.service.SystemPropertiesService;

import java.util.ArrayList;
import java.util.List;

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
		log.info("... makeLearning");
		SystemPropertiesVo properties = systemPropertiesService.retrieveSystemProperties();
		if (properties.getDeeplearningUri().length() > 0) {
			log.info("make learning " + properties.getDeeplearningUri());
			WorkloadVo workload = getWorkload();
			RestTemplate rest = new RestTemplate();
			String result
					= rest.postForObject(properties.getDeeplearningUri(), workload, String.class);
			log.info("result: {}", result);
		}
	}

	public WorkloadVo getWorkload() {
		log.info("... getWorkload");
		Connection connection = adminConnectionService.getConnection();
		SystemPropertiesVo properties
				= systemPropertiesService.retrieveSystemProperties();
		WorkloadVo workload
				= KarajanModelsKt.toWorkloadVo(properties, connection);
		return workload;
	}

	private List<WorkloadVmVo> getVms(Connection connection, String clusterName) {
		log.info("... getVms");
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
