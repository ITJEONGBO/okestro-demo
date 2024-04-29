package com.itinfo.itcloud.service;

import org.springframework.stereotype.Service;

@Service
public interface ItDashboardService {
//    DashboardVo getDashboard();

    int getDatacenters(String type); // type: up, down, ""(all)
    int getClusters();
    int gethosts(String type);
    int getvms(String type);
    int getStorages(String type);
    int getEvents(String type);


    int getCpu(String type);
    int getMemory(String type);
    int getStorage(String type);

}
