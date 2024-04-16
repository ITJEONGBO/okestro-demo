package com.itinfo.itcloud.service.computing;

import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.ReportedDevice;
import org.ovirt.engine.sdk4.types.Statistic;
import org.ovirt.engine.sdk4.types.Vm;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class CommonService {

    // BigInteger : static의 메모리, swap, speed
    public BigInteger getSpeed(List<Statistic> statisticList, String q){
        return statisticList.stream()
                .filter(statistic -> statistic.name().equals(q))
                .map(statistic -> statistic.values().get(0).datum().toBigInteger())
                .findAny()
                .orElse(BigInteger.ZERO);
    }


    // int : hugepage
    public int getPage(List<Statistic> statisticList, String q) {
        return statisticList.stream()
                .filter(statistic -> statistic.name().equals(q))
                .map(statistic -> statistic.values().get(0).datum().intValue())
                .findAny()
                .orElse(0);
    }


    // vm uptime에서 사용
    public String getVmUptime(SystemService system, String id){
        List<Statistic> statisticList = system.vmsService().vmService(id).statisticsService().list().send().statistics();

        long hour = statisticList.stream()
                .filter(statistic -> statistic.name().equals("elapsed.time"))
                .mapToLong(statistic -> statistic.values().get(0).datum().longValue() / (60 * 60))
                .findFirst()
                .orElse(0);

        String upTime;
        if (hour > 24) {
            upTime = hour / 24 + "일";
        } else if (hour > 1 && hour < 24) {
            upTime = hour + "시간";
        } else if (hour == 0) {
            upTime = null;
        } else {
            upTime = (hour / 60) + "분";
        }

        return upTime;
    }

    public String getVmIp(SystemService system, String id, String version){
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        return system.vmsService().vmService(id).nicsService().list().send().nics().stream()
                .flatMap(nic -> {
                    List<ReportedDevice> reportedDeviceList = system.vmsService().vmService(id).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice().stream()
                            .filter(r -> !vm.status().value().equals("down"))
                            .collect(Collectors.toList());
                    return reportedDeviceList.stream();
                })
                .findFirst()
                .map(r -> {
                    if ("v4".equals(version)) {
                        return r.ips().get(0).address();
                    } else {
                        return r.ips().get(1).address();
                    }
                })
                .orElse(null);
    }


}
