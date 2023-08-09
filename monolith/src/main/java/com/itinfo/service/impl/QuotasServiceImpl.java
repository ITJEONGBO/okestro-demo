package com.itinfo.service.impl;

import com.itinfo.SystemServiceHelper;
import com.itinfo.model.*;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.service.QuotasService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.QuotaClusterLimitBuilder;
import org.ovirt.engine.sdk4.builders.QuotaStorageLimitBuilder;
import org.ovirt.engine.sdk4.services.SystemService;

import org.ovirt.engine.sdk4.types.DataCenter;
import org.ovirt.engine.sdk4.types.Event;
import org.ovirt.engine.sdk4.types.Quota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuotasServiceImpl extends BaseService implements QuotasService {
	@Autowired private ConnectionService connectionService;
	@Override
	public List<QuotaVo> retrieveQuotas() {
		log.info("... retrieveQuotas");
		Connection connection = connectionService.getConnection();
		String dataCenterId
				= getSysSrvHelper().findAllDataCenters(connection).get(0).id();
		List<Quota> quotas
				= getSysSrvHelper().findAllQuotasFromDataCenter(connection, dataCenterId);
		return ModelsKt.toQuotaVos(quotas, connection);
	}

	@Override
	public QuotaVo retrieveQuotaDetail(String quotaId) {
		log.info("... retrieveQuotaDetail('{}')", quotaId);
		Connection connection = connectionService.getConnection();
		String dataCenterId
				= getSysSrvHelper().findAllDataCenters(connection).get(0).id();
		Quota quota
				= getSysSrvHelper().findQuotaFromDataCenter(connection, dataCenterId, quotaId);
		return ModelsKt.toQuotaVo(quota, connection);
	}

	@Override
	public List<EventVo> retrieveQuotaEvents(String quotaId) {
		log.info("... retrieveQuotaEvents('{}')", quotaId);
		Connection connection = connectionService.getConnection();
		List<Event> events
				= SystemServiceHelper.getInstance().findAllEvents(connection, " Quota = test-quota");
		return ModelsKt.toEventVos(events);
	}

	@Override
	public QuotaCreateVo createQuota(QuotaCreateVo quotaCreateVo) {
		log.info("... createQuota");
		Connection connection = connectionService.getConnection();
		SystemService systemService = connection.systemService();
		DataCenter dataCenter
				= systemService.dataCentersService().list().send().dataCenters().get(0);
		org.ovirt.engine.sdk4.services.QuotasService quotasService
				= systemService.dataCentersService().dataCenterService(dataCenter.id()).quotasService();
		QuotaClusterLimitVo quotaClusterLimitVo = new QuotaClusterLimitVo();
		QuotaStorageLimitBuilder qslb = new QuotaStorageLimitBuilder();
		List<QuotaClusterLimitVo> qcList = quotaCreateVo.getQuotaClusterList();
		List<QuotaStorageLimitVo> qsdList = quotaCreateVo.getQuotaStorageDomainList();
		QuotaClusterLimitBuilder qclb = new QuotaClusterLimitBuilder();
		Quota q2Add =
				ModelsKt.toQuota(quotaCreateVo);
		Quota quota
				= SystemServiceHelper.getInstance().addQuotaFromDataCenter(connection, dataCenter.id(), q2Add);
		// TODO 진행사항 확인 후 첨삭
		return null;
	}

	@Override
	public QuotaCreateVo updateQuota(QuotaCreateVo quotaCreateVo) {
		log.info("... updateQuota");
		// TODO: 처리내용 확인필요
		return null;
	}
}
