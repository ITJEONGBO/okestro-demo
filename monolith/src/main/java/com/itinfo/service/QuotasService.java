package com.itinfo.service;

import com.itinfo.model.EventVo;
import com.itinfo.model.QuotaCreateVo;
import com.itinfo.model.QuotaVo;
import java.util.List;

public interface QuotasService {
	List<QuotaVo> retrieveQuotas();

	QuotaVo retrieveQuotaDetail(String quotaId);

	List<EventVo> retrieveQuotaEvents(String quotaId);

	QuotaCreateVo createQuota(QuotaCreateVo quotaCreateVo);

	QuotaCreateVo updateQuota(QuotaCreateVo quotaCreateVo);
}

