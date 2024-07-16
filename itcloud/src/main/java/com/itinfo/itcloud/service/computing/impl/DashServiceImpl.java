package com.itinfo.itcloud.service.computing.impl;

import com.itinfo.itcloud.repository.StorageRepository;
import com.itinfo.itcloud.repository.VmRepository;
import com.itinfo.itcloud.repository.dto.HostUsageDto;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.repository.HostRepository;
import com.itinfo.itcloud.repository.dto.StorageUsageDto;
import com.itinfo.itcloud.repository.dto.UsageDto;
import com.itinfo.itcloud.service.computing.ItDashService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.StorageDomain;
import org.ovirt.engine.sdk4.types.Vm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashServiceImpl implements ItDashService {
    @Autowired private AdminConnectionService admin;
    @Autowired private HostRepository hostRepository;
    @Autowired private VmRepository vmRepository;
    @Autowired private StorageRepository storageRepository;

    private final double GB = 1073741824; // gb 변환

    /**
     * 전체 사용량 - Host (CPU, Memory  % ) 원 그래프
     * @return 5분마다 한번씩 불려지게 해야함
     */
    @Override
    public HostUsageDto totalCpuMemory() {
        SystemService system = admin.getConnection().systemService();
        List<Host> hostList = system.hostsService().list().send().hosts();

        int hostCnt = hostList.size();

        double total = hostList.stream().mapToDouble(Host::memoryAsLong).sum() / GB;
        double used = hostList.stream()
                .flatMap(host -> system.hostsService().hostService(host.id()).statisticsService().list().send().statistics().stream())
                .filter(stat -> "memory.used".equals(stat.name()))
                .mapToDouble(stat -> stat.values().get(0).datum().doubleValue())
                .sum() / GB;
        double free = total - used;

        double totalCpu = 0;
        double totalMemory = 0;
        LocalDateTime time = null;
        for(Host host : hostList){
            HostUsageDto usage = hostRepository.findFirstByHostIdOrderByHistoryDatetimeDesc(UUID.fromString(host.id())).totalCpuMemory();
            totalCpu += usage.getTotalCpuUsagePercent();
            totalMemory += usage.getTotalMemoryUsagePercent();
            time = usage.getHistoryDatetime();
        }

        return HostUsageDto.builder()
                .historyDatetime(time)
                .totalCpuUsagePercent(Math.round(totalCpu / hostCnt))
                .totalMemoryUsagePercent(Math.round(totalMemory / hostCnt))
                .totalMemoryGB(total)
                .usedMemoryGB(used)
                .freeMemoryGB(free)
                .build();
    }


    /**
     * 전체 사용량 - Storage % 원 그래프
     * @return 스토리지 사용량
     */
    @Override
    public StorageUsageDto totalStorage() {
        SystemService system = admin.getConnection().systemService();
        List<StorageDomain> storageDomainList = system.storageDomainsService().list().send().storageDomains();

        double free = storageDomainList.stream()
                .filter(StorageDomain::availablePresent)
                .mapToDouble(StorageDomain::availableAsLong)
                .sum() / GB;

        double used = storageDomainList.stream()
                .filter(StorageDomain::usedPresent)
                .mapToDouble(StorageDomain::usedAsLong)
                .sum() / GB;

        return StorageUsageDto.builder()
                .totalGB(free + used)
                .freeGB(free)
                .usedGB(used)
                .usedPercent(used / (free + used) * 100)
                .build();

    }



    /**
     * 전체 사용량(CPU, Memory %) 선 그래프
     * @param hostId 호스트 id
     * @return 10분마다 그래프에 찍히게?
     */
    @Override
    public List<HostUsageDto> totalCpuMemoryList(UUID hostId, int limit) {
        SystemService system = admin.getConnection().systemService();
        Pageable page = PageRequest.of(0, limit);
        String hostName = system.hostsService().list().search("id=" + hostId).send().hosts().get(0).name();

        return hostRepository.findByHostIdOrderByHistoryDatetimeDesc(hostId, page).stream()
                .map(hostEntity ->
                        HostUsageDto.builder()
                            .hostName(hostName)
                            .historyDatetime(hostEntity.getHistoryDatetime())
                            .totalCpuUsagePercent(hostEntity.getCpuUsagePercent())
                            .totalMemoryUsagePercent(hostEntity.getMemoryUsagePercent())
                            .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 cpu 사용량 3
     * @return
     */
    @Override
    public List<UsageDto> vmCpuChart() {
        SystemService system = admin.getConnection().systemService();
        Pageable page = PageRequest.of(0, 3);

        return vmRepository.findVmCpuChart(page).stream()
                .map(vmEntity -> {
                    Vm vm = system.vmsService().vmService(String.valueOf(vmEntity.getVmId())).get().send().vm();
                    return UsageDto.builder()
                            .name(vm.name())
                            .cpuPercent(vmEntity.getCpuUsagePercent())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 memory 사용량 3
     * @return
     */
    @Override
    public List<UsageDto> vmMemoryChart() {
        SystemService system = admin.getConnection().systemService();
        Pageable page = PageRequest.of(0, 3);

        return vmRepository.findVmMemoryChart(page).stream()
                .map(vmEntity -> {
                    Vm vm = system.vmsService().vmService(String.valueOf(vmEntity.getVmId())).get().send().vm();
                    return UsageDto.builder()
                            .name(vm.name())
                            .memoryPercent(vmEntity.getMemoryUsagePercent())
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<UsageDto> storageChart() {
        SystemService system = admin.getConnection().systemService();
        Pageable page = PageRequest.of(0, 3);

        return storageRepository.findStorageChart(page).stream()
                .map(domainEntity -> {
                    StorageDomain storageDomain = system.storageDomainsService().storageDomainService(String.valueOf(domainEntity.getStorageDomainId())).get().send().storageDomain();
                    int totalGB = domainEntity.getAvailableDiskSizeGb() + domainEntity.getUsedDiskSizeGb();

//                    System.out.println(domainEntity.getUsedDiskSizeGb());
//                    System.out.println(totalGB);

                    return UsageDto.builder()
                            .name(storageDomain.name())
                            .memoryPercent((domainEntity.getUsedDiskSizeGb() / totalGB) * 100)
                            .build();
                })
                .collect(Collectors.toList());
    }


}
