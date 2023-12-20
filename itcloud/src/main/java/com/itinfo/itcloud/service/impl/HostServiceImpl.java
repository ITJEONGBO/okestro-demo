package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItHostService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HostServiceImpl implements ItHostService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    public HostServiceImpl(){}

    @Override
    public List<HostVO> getHosts() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<HostVO> hostVOList = new ArrayList<>();
        HostVO hostVO = null;

        List<Host> hostList =
                ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();

        for(Host host : hostList) {
            hostVO = new HostVO();

            hostVO.setId(host.id());
            hostVO.setName(host.name());
            hostVO.setComment(host.comment());
            hostVO.setAddress(host.address());
            hostVO.setStatus(host.status().value());

//            hostVO.setClusterId();
//            hostVO.setDatacenterId();

//            hostVO.setVmCnt();
            System.out.println("SPM : "+host.spm().status().value());
            hostVO.setSpm(host.spm().status().value());
            hostVOList.add(hostVO);
        }
        return hostVOList;
    }

    @Override
    public List<HostVO> getList() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<HostVO> hostVOList = new ArrayList<>();
        HostVO hostVO = null;

        List<Host> hostList =
                ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();

        for(Host host : hostList){
            hostVO = new HostVO();

            hostVO.setId(host.id());
            hostVO.setName(host.name());
            hostVO.setComment(host.comment());
            hostVO.setAddress(host.address());
            hostVO.setStatus(host.status().value());

            Cluster cluster =
                    ((ClusterService.GetResponse)systemService.clustersService().clusterService(host.cluster().id()).get().send()).cluster();
            hostVO.setClusterId(cluster.id());
            hostVO.setClusterName(cluster.name());
            hostVO.setDatacenterId(cluster.dataCenter().id());

            DataCenter dataCenter =
                    ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter();
            hostVO.setDatacenterName(dataCenter.name());

            List<Vm> vmList =
                    ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();


            int vmsCnt = 0;
            for(Vm vm : vmList){
                if(vm.host()!= null && vm.host().id().equals(host.id())){
                    vmsCnt++;
                }
            }

            hostVO.setVmCnt(vmsCnt);
            hostVO.setSpm(host.spm().status().value());
            hostVOList.add(hostVO);
        }
        return hostVOList;
    }

    @Override
    public HostVO getInfo(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        HostVO hostVO = new HostVO();
        hostVO.setId(id);

        Host host =
                ((HostService.GetResponse)systemService.hostsService().hostService(id).get().send()).host();

        hostVO.setName(host.name());
        hostVO.setAddress(host.address()); //호스트 ip
        hostVO.setSpmPriority(host.spm().priorityAsInteger());  // spm 우선순위

        int cpu = host.cpu().topology().coresAsInteger()
                * host.cpu().topology().socketsAsInteger()
                * host.cpu().topology().threadsAsInteger();
        hostVO.setCpuCoreCnt(cpu); // 논리 cpu 코어수

        if(host.iscsi() != null){
            hostVO.setIscsi(host.iscsi().initiator());  // iscsi 게시자 이름
        }

        hostVO.setKdumpStatus(host.kdumpStatus().value());  // kdump intergration status
        hostVO.setDevicePassthrough(host.devicePassthrough().enabled());    // 장치통과
        hostVO.setNewVmMemory(host.maxSchedulingMemory());      // 최대 여유 메모리

        //메모리 페이지 공유  비활성


        // 자동으로 페이지를 크게 (확실하지 않음. 매우)
//        hostVO.setPageSize(host.transparentHugePages().enabled());

        // selinux모드
        hostVO.setSeLinux(host.seLinux().mode().value());

        // 클러스터 호환버전


        int a = 0;
        int aa = 0;
        int b = 0;
        int bb = 0;

        List<Statistic> statisticList =
                ((StatisticsService.ListResponse)systemService.hostsService().hostService(id).statisticsService().list().send()).statistics();

        for(Statistic statistic : statisticList){
            // 물리메모리
            if(statistic.name().equals("memory.total")){
                hostVO.setMemory(statistic.values().get(0).datum().toBigInteger());
            }
            if(statistic.name().equals("memory.used")){
                hostVO.setUsedMemory(statistic.values().get(0).datum().toBigInteger());
            }
            if(statistic.name().equals("memory.free")){
                hostVO.setFreeMemory(statistic.values().get(0).datum().toBigInteger());
            }

            // swap 크기
            if(statistic.name().equals("swap.total")){
                hostVO.setSwapMemory(statistic.values().get(0).datum().toBigInteger());
            }
            if(statistic.name().equals("swap.used")){
                hostVO.setSwapUsedMemory(statistic.values().get(0).datum().toBigInteger());
            }
            if(statistic.name().equals("swap.free")){
                hostVO.setSwapFreeMemory(statistic.values().get(0).datum().toBigInteger());
            }

            // 부팅시간
            if(statistic.name().equals("boot.time")){
                hostVO.setBootTime(statistic.values().get(0).datum());
            }


            // Huge pages(size:free/total)
            if(statistic.name().equals("hugepages.2048.free")){
                a = statistic.values().get(0).datum().intValue();
            }
            if(statistic.name().equals("hugepages.2048.total")){
                aa =  statistic.values().get(0).datum().intValue();
            }

            if(statistic.name().equals("hugepages.1048576.free")){
                b = statistic.values().get(0).datum().intValue();
            }
            if(statistic.name().equals("hugepages.1048576.total")){
                bb = statistic.values().get(0).datum().intValue();
            }
        }

        // 코드 수정 필요
        hostVO.setHugePagesType("2048: " + a + "/" + aa);
        hostVO.setHugePagesType2("1048576: " + b + "/" + b);

        // 가상머신 수
        List<Vm> vmList =
                ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        int vmsUpCnt = 0;
        for(Vm vm : vmList){
            if(vm.host()!= null && vm.host().id().equals(host.id()) && vm.status().value().equals("up")){
                vmsUpCnt++;
            }
        }

        hostVO.setVmUpCnt(vmsUpCnt);
        getHardWare(systemService, hostVO, id);
        getSoftWare(systemService, hostVO, id);

        return hostVO;
    }

    public void getHardWare(SystemService systemService, HostVO hostVO, String id){
        Host host =
                ((HostService.GetResponse)systemService.hostsService().hostService(id).get().send()).host();

        HostHwVO hostHwVO = new HostHwVO();
        // 생산자
        hostHwVO.setFamily(host.hardwareInformation().family());
        // 제품군
        hostHwVO.setManufacturer(host.hardwareInformation().manufacturer());
        // 제품 이름
        hostHwVO.setProductName(host.hardwareInformation().productName());
        // 버전
        hostHwVO.setHwVersion(host.hardwareInformation().version());
        // cpu 모델
        hostHwVO.setCpuName(host.cpu().name());
        // cpu 유형
        hostHwVO.setCpuType(host.cpu().type());
        // uuid
        hostHwVO.setUuid(host.hardwareInformation().uuid());
        // 일련번호
        hostHwVO.setSerialNum(host.hardwareInformation().serialNumber());
        // 코어당 cpu 스레드
//        hostHwVO.setCoreThread();
        // 소켓당 cpu 코어
//        hostHwVO.setSocketCore();
        // cpu 소켓
        hostHwVO.setCpuSocket(host.cpu().topology().socketsAsInteger());

        hostVO.setHostHwVO(hostHwVO);
    }

    public void getSoftWare(SystemService systemService, HostVO hostVO, String id){
        Host host =
                ((HostService.GetResponse)systemService.hostsService().hostService(id).get().send()).host();

        HostSwVO hostSwVO = new HostSwVO();

        // 구하는 방법이 db밖에는 없는건지 확인필요

        // os 버전
        hostSwVO.setOsVersion(host.os().type() + " " + host.os().version().fullVersion());
        // os 정보
//        hostSwVO.setOsInfo(host.os().);
        // 커널 버전 db 수정해야함
        hostSwVO.setKernalVersion(host.os().reportedKernelCmdline());
//        System.out.println(hostSwVO.getKernalVersion());
        // kvm 버전 db
        // LIBVIRT 버전
        hostSwVO.setLibvirtVersion(host.libvirtVersion().fullVersion());
        // VDSM 버전 db
        hostSwVO.setVdsmVersion(host.version().fullVersion());
        // SPICE 버전
        // GlusterFS 버전
        // CEPH 버전
        // Open vSwitch 버전
        // Nmstate 버전

        hostVO.setHostSwVO(hostSwVO);
    }


    @Override
    public HostVO getVm(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        HostVO hostVO = new HostVO();
        hostVO.setId(id);

        List<VmVO> vmVOList = new ArrayList<>();
        VmVO vmVO = null;

        List<Vm> vmList =
                ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

        for (Vm vm : vmList) {
            if (vm.host() != null && vm.host().id().equals(id)) {
                vmVO = new VmVO();

                vmVO.setVmId(vm.id());
                vmVO.setVmName(vm.name());
                vmVO.setStatus(vm.status().value());
                vmVO.setFqdn(vm.fqdn());

                // uptime
                if(vm.startTime() != null) {
                    vmVO.setStartTime(vm.startTime().toString());
                }

                if(!vm.status().value().equals("down")){
                    // ipv4 부분. vms-nic-reporteddevice
                    List<Nic> nicList =
                            ((VmNicsService.ListResponse) systemService.vmsService().vmService(vm.id()).nicsService().list().send()).nics();

                    for (Nic nic : nicList){
                        List<ReportedDevice> reportedDeviceList
                                = ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send()).reportedDevice();
                        for (ReportedDevice r : reportedDeviceList){
                            vmVO.setIpv4(r.ips().get(0).address());
                            vmVO.setIpv6(r.ips().get(1).address());
                        }
                    }
                }else{
                    vmVO.setIpv4("");
                    vmVO.setIpv6("");
                }
                vmVOList.add(vmVO);
            }
        }
        hostVO.setVmVOList(vmVOList);

        return hostVO;
    }

    @Override
    public HostVO getNic(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        HostVO hostVO = new HostVO();
        hostVO.setId(id);

        List<HostNicVO> hostNicVOList = new ArrayList<>();
        HostNicVO hostNicVO = null;
        List<HostNic> hostNicList =
                ((HostNicsService.ListResponse)systemService.hostsService().hostService(id).nicsService().list().send()).nics();

        for(HostNic hostNic : hostNicList){
            hostNicVO = new HostNicVO();

            hostNicVO.setId(hostNic.id());
            hostNicVO.setName(hostNic.name());
            hostNicVO.setMacAddress(hostNic.mac().address());
            hostNicVO.setSpeed(hostNic.speed());

            hostNicVOList.add(hostNicVO);
        }
        hostVO.setHostNicVOList(hostNicVOList);
        return hostVO;
    }

    @Override
    public HostVO getHostDevice(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        HostVO hostVO = new HostVO();
        hostVO.setId(id);

        List<HostDeviceVO> hostDeviceVOList = new ArrayList<>();
        HostDeviceVO hostDeviceVO = null;

        List<HostDevice> hostDeviceList =
                ((HostDevicesService.ListResponse)systemService.hostsService().hostService(id).devicesService().list().send()).devices();

        for(HostDevice hostDevice : hostDeviceList){
            hostDeviceVO = new HostDeviceVO();

            hostDeviceVO.setName(hostDevice.name());
            hostDeviceVO.setCapability(hostDevice.capability());

            if(hostDevice.vendor() != null){
                hostDeviceVO.setVendor(hostDevice.vendor().name());
            }
            if(hostDevice.product() != null){
                hostDeviceVO.setProduct(hostDevice.product().name());
            }
            if(hostDevice.driver() != null){
                hostDeviceVO.setDriver(hostDevice.driver());
            }

            hostDeviceVOList.add(hostDeviceVO);
        }
        hostVO.setHostDeviceVOList(hostDeviceVOList);
        // hostvo를 쓸필요없는데 왜 쓰고 이썽쓸까
        return hostVO;
    }

    @Override
    public HostVO getPermission(String id) {
        return null;
    }

    @Override
    public HostVO getAffinitylabels(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        HostVO hostVO = new HostVO();
        hostVO.setId(id);

        List<AffinityLabelVO> affinityLabelVOList = new ArrayList<>();
        AffinityLabelVO affinityLabelVO = null;

        List<AffinityLabel> affinityLabelList =
                ((AffinityLabelsService.ListResponse)systemService.hostsService().hostService(id).affinityLabelsService().list().send()).labels();

        for(AffinityLabel a : affinityLabelList) {
            affinityLabelVO = new AffinityLabelVO();

            affinityLabelVO.setId(a.id());
            affinityLabelVO.setName(a.name());

            affinityLabelVOList.add(affinityLabelVO);
        }

        hostVO.setAffinityLabelVOList(affinityLabelVOList);
        return hostVO;
    }




}
