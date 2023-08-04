package com.itinfo.service;

import com.itinfo.model.DataCenterVo;
import com.itinfo.model.EventVo;
import java.util.List;

public interface DashboardService {
    DataCenterVo retrieveDataCenterStatus();
    List<EventVo> retrieveEvents();
}
