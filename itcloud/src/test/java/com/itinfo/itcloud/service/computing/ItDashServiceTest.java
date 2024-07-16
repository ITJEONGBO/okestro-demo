package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.repository.dto.HostUsageDto;
import com.itinfo.itcloud.repository.dto.StorageUsageDto;
import com.itinfo.itcloud.repository.dto.UsageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class ItDashServiceTest {
    @Autowired ItDashService dashService;

    private final String defaultId = "971160c2-307d-463b-8f52-459561aa6996";    // 70 host01

    @Test
    @DisplayName("전체사용량 - 원 그래프 cpu,memory %")
    void totalUsage() {
        HostUsageDto result = dashService.totalCpuMemory();
        System.out.println("host1 " + result);
    }

    @Test
    @DisplayName("전체사용량 - 스토리지 ")
    void totalStorage() {
        StorageUsageDto result = dashService.totalStorage();
        System.out.println(result);
    }

    @Test
    @DisplayName("전체사용량 - 선 그래프 cpu,memory %")
    void totalUsageList() {
        List<HostUsageDto> result = dashService.totalCpuMemoryList(UUID.fromString(defaultId), 5);

        System.out.println("host" + result.size());

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("전체사용량 - vm cpu top 3 %")
    void vmCpu() {
        List<UsageDto> result = dashService.vmCpuChart();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("전체사용량 - vm memory top 3 %")
    void vmMemory() {
        List<UsageDto> result = dashService.vmMemoryChart();

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("전체사용량 - 스토리지 %?GB?")
    void storageChart() {
        List<UsageDto> result = dashService.storageChart();

        result.forEach(System.out::println);
    }




    @Test
    @DisplayName("전체사용량 - host cpu top 3 %")
    void hostCpu() {
        List<UsageDto> result = dashService.hostCpuChart();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("전체사용량 - host memory top 3 %")
    void hostMemory() {
        List<UsageDto> result = dashService.hostMemoryChart();

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("전체사용량 - host network %")
    void hostNetwork() {
//        List<UsageDto> result = dashService.
//
//        result.forEach(System.out::println);
    }






}