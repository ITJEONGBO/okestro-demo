package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.NicVO;
import com.itinfo.itcloud.model.computing.VmDiskVO;
import com.itinfo.itcloud.model.computing.VmVO;
import com.itinfo.itcloud.model.storage.DiskVO;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItVmService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


// r저거 고쳐야됨 model
@Service
public class VmServiceImpl implements ItVmService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    public VmServiceImpl(){}


    @Override
    public List<VmVO> getVms() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VmVO> vmVOList = new ArrayList<>();
        VmVO vmVO = null;

        List<Vm> vmList =
                ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        for(Vm vm : vmList){
            vmVO = new VmVO();

            vmVO.setId(vm.id());
            vmVO.setName(vm.name());
            vmVO.setStatus(vm.status().value());
            vmVO.setStartTime(String.valueOf(vm.startTime()));  // 고쳐야됨
            vmVO.setTemplateID(vm.template().id());
            vmVO.setTemplateName(vm.template().name());
            vmVO.setOs(vm.os().type());
            vmVO.setPriority(String.valueOf(vm.highAvailability().priority()));  //타입수정?
            vmVO.setOptimizeOption(String.valueOf(vm.type()));



        }

        return null;
    }

    @Override
    public List<VmVO> getList() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VmVO> vmVOList = new ArrayList<>();
        VmVO vmVO = null;

        List<Vm> vmList =
                ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        for(Vm vm : vmList){
            vmVO = new VmVO();

            vmVO.setStatus(vm.status().value());    // 상태는 두번표시됨. 그림과 글자로
            vmVO.setId(vm.id());
            vmVO.setName(vm.name());
            vmVO.setDescription(vm.description());

//            vmVO.setHostId();
//            vmVO.setHostName();

            Cluster cluster =
                    ((ClusterService.GetResponse)systemService.clustersService().clusterService(vm.cluster().id()).get().send()).cluster();
            vmVO.setClusterId(cluster.id());
            vmVO.setClusterName(cluster.name());
            vmVO.setDataCenterId(cluster.dataCenter().id());

            DataCenter dataCenter =
                    ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter();
            vmVO.setDataCenterName(dataCenter.name());

            vmVO.setFqdn(vm.fqdn());

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
            System.out.println("vmVO: "+vmVO.toString());
        }
        return vmVOList;
    }

    @Override
    public VmVO getInfo(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        VmVO vmVO = new VmVO();
        vmVO.setId(id);

        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();

        vmVO.setStatus(vm.status().value());    // 상태는 두번표시됨. 그림과 글자로
        vmVO.setName(vm.name());
        vmVO.setDescription(vm.description());
        vmVO.setTemplateName(vm.template().name());
        vmVO.setOs(vm.os().type());
        vmVO.setChipsetFirmType(vm.bios().type().value());
        vmVO.setPriority(String.valueOf(vm.highAvailability().priority()));  //타입수정?
        vmVO.setOptimizeOption(String.valueOf(vm.type()));  // 최적화 옵션
        System.out.println(vmVO.getOptimizeOption());

        vmVO.setMemory(vm.memory());
        vmVO.setRealMemory(vm.memoryPolicy().guaranteed());

        // 게스트os의 여유/캐시+버퍼된 메모리

        vmVO.setCpuCore(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().socketsAsInteger());
        vmVO.setGuestCpuCnt(vmVO.getCpuCore());     // 게스트 cpu 수

        // 게스트 cpu
        // 고가용성

        vmVO.setMonitor(vm.display().monitorsAsInteger());      // 모니터 수
        vmVO.setUsb(vm.usb().enabled());   // usb
        // 작성자
        // 소스
        // 실행 호스트(Host)item.placementPolicy().hosts().get(0)    ..get().send()).host().name()
        if (vm.placementPolicyPresent() && vm.placementPolicy().hostsPresent()) {
            vmVO.setRunHost(((HostService.GetResponse)systemService.hostsService().hostService(((Host)vm.placementPolicy().hosts().get(0)).id()).get().send()).host().name());
        }
        // 사용자 정의 속성
        // 클러스터 호환 버전
        // 가상머신의 id

        vmVO.setFqdn(vm.fqdn());

        vmVO.setHwTimeOffset(vm.timeZone().name());     // 하드웨어 클럭의 시간 오프셋

        if (vm.startTime() != null) {
            vmVO.setStartTime(vm.startTime().toString());
        }

        return vmVO;
    }

    @Override
    public VmVO getNic(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        VmVO vmVO = new VmVO();
        vmVO.setId(id);

        List<NicVO> nicVOList = new ArrayList<>();
        NicVO nicVO = null;

        List<Nic> nicList =
                ((VmNicsService.ListResponse)systemService.vmsService().vmService(id).nicsService().list().send()).nics();

        for(Nic nic : nicList){
            nicVO = new NicVO();

            nicVO.setId(nic.id());
            nicVO.setName(nic.name());
            if(nic.network() != null){
                nicVO.setNetworkName(nic.network().name());
            }
//            nicVO.setProfileName(nic.);
            nicVO.setLinkStatus(nic.linked());
            nicVO.setPlugged(nic.plugged());
            nicVO.setType(nic.interface_().value());

//            nicVO.setIpv4(nic.reportedDevices().get(0).ips().get(0).address());
//            nicVO.setIpv4(nic.reportedDevices().get(0).ips().get(1).address());
//            System.out.println(nicVO.getIpv4() + "/ "+nicVO.getIpv6());

            nicVOList.add(nicVO);
        }
        vmVO.setNicVOList(nicVOList);
        return vmVO;
    }

    @Override
    public VmVO getDisk(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        VmVO vmVO = new VmVO();
        vmVO.setId(id);

        List<VmDiskVO> vmDiskVOList = new ArrayList<>();
        VmDiskVO vmDiskVO = null;


        List<DiskAttachment> vmdiskList =
                ((DiskAttachmentsService.ListResponse)systemService.vmsService().vmService(id).diskAttachmentsService().list().send()).attachments();
        // 별칭, 가상크기, 연결대상, 인터페이스, 논리적 이름, 상태, 유형, 설명

        for(DiskAttachment disk : vmdiskList){
            vmDiskVO = new VmDiskVO();
            vmDiskVO.setDiskId(disk.id());
            vmDiskVO.setInterfaceName(disk.interface_().value());
            vmDiskVO.setReadOnly(disk.readOnly());
            vmDiskVO.setBootable(disk.bootable());
            vmDiskVO.setLogicalName(disk.logicalName());
            vmDiskVO.setAcitve(disk.active());

            Disk disk1 =
                    ((DiskService.GetResponse)systemService.disksService().diskService(disk.id()).get().send()).disk();

            DiskVO diskVO = new DiskVO();
            diskVO.setId(disk1.id());
            diskVO.setDescription(disk1.description());
//            diskVO.setVirtualSize(disk1.);
            diskVO.setStorageType(disk1.storageType().value());
            diskVO.setContentType(disk1.contentType().value());

            vmDiskVO.setDiskVO(diskVO);
            vmDiskVOList.add(vmDiskVO);
        }
        vmVO.setVmDiskVOList(vmDiskVOList);

        return vmVO;
    }

    @Override
    public VmVO getSnapshot(String id) {
        return null;
    }

    @Override
    public VmVO getApplication(String id) {
        return null;
    }

    @Override
    public VmVO getAffinitygroup(String id) {
        return null;
    }

    @Override
    public VmVO getAffinitylabel(String id) {
        return null;
    }

    @Override
    public VmVO getGuestInfo(String id) {
        return null;
    }

    @Override
    public VmVO getPermission(String id) {
        return null;
    }

    @Override
    public VmVO getEvent(String id) {
        return null;
    }
}
