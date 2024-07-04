package com.itinfo.itcloud.service;

import com.itinfo.itcloud.dao.enitiy.HostSamplesHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItDashboardService {
//    DashboardVo getDashboard();

    int getDatacenters(String type); // type: up, down, ""(all)
    int getClusters();
    int gethosts(String type);
    int getvms(String type);
    int getStorages();
    int getEvents(String type);

    int getCpu();
    int getMemory(String type);
    int getStorage(String type);

}
