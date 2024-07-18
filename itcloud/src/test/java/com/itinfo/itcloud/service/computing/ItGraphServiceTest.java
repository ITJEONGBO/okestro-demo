package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.DashBoardVo;
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
class ItGraphServiceTest {
    @Autowired
    ItGraphService graphService;

    private final String defaultId = "971160c2-307d-463b-8f52-459561aa6996";    // 70 host01



    @Test
    @DisplayName("대시보드 ")
    void getDashboard(){
        DashBoardVo result = graphService.getDashboard();

        System.out.println(result);
    }


    @Test
    @DisplayName("전체사용량 - 원 그래프 cpu,memory %")
    void totalUsage() {
        HostUsageDto result = graphService.totalCpuMemory();
        System.out.println("host1 " + result);
    }

    @Test
    @DisplayName("전체사용량 - 스토리지 ")
    void totalStorage() {
        StorageUsageDto result = graphService.totalStorage();
        System.out.println(result);
    }

    @Test
    @DisplayName("전체사용량 - 선 그래프 cpu,memory %")
    void totalUsageList() {
        List<HostUsageDto> result = graphService.totalCpuMemoryList(UUID.fromString(defaultId), 5);

        System.out.println("host" + result.size());

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("전체사용량 - vm cpu top 3 %")
    void vmCpuChart() {
        List<UsageDto> result = graphService.vmCpuChart();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("전체사용량 - vm memory top 3 %")
    void vmMemoryChart() {
        List<UsageDto> result = graphService.vmMemoryChart();

        result.forEach(System.out::println);
    }


    @Test
    @DisplayName("전체사용량 - 스토리지 %?GB?")
    void storageChart() {
        List<UsageDto> result = graphService.storageChart();

        result.forEach(System.out::println);
    }




    @Test
    @DisplayName("전체사용량 - host cpu top 3 %")
    void hostCpuChart() {
        List<UsageDto> result = graphService.hostCpuChart();

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("전체사용량 - host memory top 3 %")
    void hostMemoryChart() {
        List<UsageDto> result = graphService.hostMemoryChart();

        result.forEach(System.out::println);
    }




//    @Test
//    @DisplayName("host cpu, memory %")
//    void hostCpuMemory() {
//        UsageDto result = dashService.hostPercent(defaultId);
//
//        System.out.println(result);
//    }







}