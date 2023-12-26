package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.DashboardVo;
import org.springframework.stereotype.Service;

@Service
public interface ItDashboardService {
    DashboardVo getDashboard();

}
