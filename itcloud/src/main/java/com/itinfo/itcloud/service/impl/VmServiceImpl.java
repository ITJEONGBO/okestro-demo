package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.*;
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


    @Override
    public List<VmVo> getList() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;

        List<Vm> vmList =
                ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        for(Vm vm : vmList){
            vmVo = new VmVo();

            vmVo.setStatus(vm.status().value());
            vmVo.setId(vm.id());
            vmVo.setName(vm.name());
            vmVo.setDescription(vm.description());

            // host
            if(vm.status().value().equals("up") && vm.hostPresent()) {
                vmVo.setHostId(vm.host().id());
                vmVo.setHostName(((HostService.GetResponse) systemService.hostsService().hostService(vm.host().id()).get().send()).host().name());
            }

            Cluster cluster =
                    ((ClusterService.GetResponse)systemService.clustersService().clusterService(vm.cluster().id()).get().send()).cluster();
            vmVo.setClusterId(cluster.id());
            vmVo.setClusterName(cluster.name());

            vmVo.setDatacenterId(cluster.dataCenter().id());
            vmVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send()).dataCenter().name() );
            vmVo.setFqdn(vm.fqdn());

            if(vm.startTime() != null) {
                vmVo.setStartTime(vm.startTime());
            }

            if(!vm.status().value().equals("down")){
                // ipv4 부분. vms-nic-reporteddevice
                List<Nic> nicList =
                        ((VmNicsService.ListResponse) systemService.vmsService().vmService(vm.id()).nicsService().list().send()).nics();

                for (Nic nic : nicList){
                    List<ReportedDevice> reportedDeviceList
                            = ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send()).reportedDevice();
                    for (ReportedDevice r : reportedDeviceList){
                        vmVo.setIpv4(r.ips().get(0).address());
                        vmVo.setIpv6(r.ips().get(1).address());
                    }
                }
            }else{
                vmVo.setIpv4("");
                vmVo.setIpv6("");
            }

            vmVoList.add(vmVo);
        }
        return vmVoList;
    }

    @Override
    public VmVo getInfo(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        VmVo vmVo = new VmVo();
        vmVo.setId(id);

        Vm vm =
                ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();

        vmVo.setStatus(vm.status().value());    // 상태는 두번표시됨. 그림과 글자로
        vmVo.setName(vm.name());
        vmVo.setDescription(vm.description());
        vmVo.setTemplateName( ((TemplateService.GetResponse)systemService.templatesService().templateService(vm.template().id()).get().send()).template().name() );

        System.out.println(((TemplateService.GetResponse)systemService.templatesService().templateService(vm.template().id()).get().send()).template().name());

        // host
        vmVo.setHostName(((HostService.GetResponse) systemService.hostsService().hostService(vm.host().id()).get().send()).host().name());

        vmVo.setOsSystem(vm.os().type());
        vmVo.setChipsetFirmwareType(vm.bios().type().value());
        vmVo.setPriority(vm.highAvailability().priorityAsInteger());  // 우선순위
        vmVo.setOptimizeOption(vm.type().value());  // 최적화 옵션

        vmVo.setMemory(vm.memory());
        vmVo.setMemoryActual(vm.memoryPolicy().guaranteed());

        // 게스트os의 여유/캐시+버퍼된 메모리

        vmVo.setCpuCoreCnt(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().socketsAsInteger());
//        vmVo.setGuestCpuCnt(vmVo.getGuestCpuCnt());     // 게스트 cpu 수

        // 게스트 cpu
        // 고가용성

        vmVo.setMonitor(vm.display().monitorsAsInteger());      // 모니터 수
        vmVo.setUsb(vm.usb().enabled());   // usb
        // 작성자
        // 소스
        // 실행 호스트(Host)item.placementPolicy().hosts().get(0)    ..get().send()).host().name()
//        if (vm.placementPolicyPresent() && vm.placementPolicy().hostsPresent()) {
//            vmVo.setRunHost(((HostService.GetResponse)systemService.hostsService().hostService(((Host)vm.placementPolicy().hosts().get(0)).id()).get().send()).host().name());
//        }
        // 사용자 정의 속성
        // 클러스터 호환 버전
        // 가상머신의 id

        vmVo.setFqdn(vm.fqdn());

        vmVo.setHwTimeOffset(vm.timeZone().name());     // 하드웨어 클럭의 시간 오프셋

        if (vm.startTime() != null) {
            vmVo.setStartTime(vm.startTime());
        }

        return vmVo;
    }

    @Override
    public List<NicVo> getNic(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<NicVo> nVoList = new ArrayList<>();
        NicVo nVo = null;

        List<Nic> nicList =
                ((VmNicsService.ListResponse)systemService.vmsService().vmService(id).nicsService().list().send()).nics();

        for(Nic nic : nicList){
            nVo = new NicVo();

            nVo.setId(nic.id());
            nVo.setName(nic.name());
            if(nic.network() != null){
                nVo.setNetworkName(nic.network().name());
            }
//            nVo.setProfileName(nic.);
            nVo.setLinkStatus(nic.linked());
            nVo.setPlugged(nic.plugged());    // 연결상태
            nVo.setType(nic.interface_().value());

//            nVo.setIpv4(nic.reportedDevices().get(0).ips().get(0).address());
//            nVo.setIpv4(nic.reportedDevices().get(0).ips().get(1).address());
//            System.out.println(nVo.getIpv4() + "/ "+nVo.getIpv6());
            nVoList.add(nVo);
        }
        return nVoList;
    }

    @Override
    public List<VmDiskVo> getDisk(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VmDiskVo> vdVoList = new ArrayList<>();
        VmDiskVo vdVo = null;

        List<DiskAttachment> vmdiskList =
                ((DiskAttachmentsService.ListResponse)systemService.vmsService().vmService(id).diskAttachmentsService().list().send()).attachments();
        // 별칭, 가상크기, 연결대상, 인터페이스, 논리적 이름, 상태, 유형, 설명

        for(DiskAttachment diskAttachment : vmdiskList){
            vdVo = new VmDiskVo();

            vdVo.setId(diskAttachment.id());
            vdVo.setActive(diskAttachment.active());
            vdVo.setReadOnly(diskAttachment.readOnly());
            vdVo.setBootAble(diskAttachment.bootable());
            vdVo.setLogicalName(diskAttachment.logicalName());
            vdVo.setInterfaceName(diskAttachment.interface_().value());

            Disk disk =
                    ((DiskService.GetResponse)systemService.disksService().diskService(diskAttachment.disk().id()).get().send()).disk();
            vdVo.setName(disk.name());
            vdVo.setDescription(disk.description());
            vdVo.setVirtualSize(disk.provisionedSize());
            vdVo.setStatus(String.valueOf(disk.status()));  // 유형
            vdVo.setType(disk.storageType().value());
            vdVo.setConnection(disk.contentType().value());

            vdVoList.add(vdVo);
        }
        return vdVoList;
    }

//    https://192.168.0.80/ovirt-engine/api/vms/931ad1d3-0782-4727-947d-6a765cfcc401/snapshots
    @Override
    public List<SnapshotVo> getSnapshot(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<SnapshotVo> sVoList = new ArrayList<>();
        SnapshotVo sVo = null;

        List<Snapshot> snapList =
                ((SnapshotsService.ListResponse)systemService.vmsService().vmService(id).snapshotsService().list().send()).snapshots();

        for(Snapshot snapshot : snapList){
            sVo = new SnapshotVo();

            sVo.setDate(snapshot.date());
            sVo.setStatus(String.valueOf(snapshot.snapshotStatus()));   // 데이터형고민
            sVo.setPersistMemory(snapshot.persistMemorystate());
            sVo.setDescription(snapshot.description());

            sVoList.add(sVo);
        }
        return sVoList;
    }

    @Override
    public List<ApplicationVo> getApplication(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<ApplicationVo> appVoList = new ArrayList<>();
        ApplicationVo appVo = null;

        List<Application> appList =
                ((VmApplicationsService.ListResponse)systemService.vmsService().vmService(id).applicationsService().list().send()).applications();

        for(Application application : appList) {
            appVo = new ApplicationVo();
            appVo.setName(application.name());

            appVoList.add(appVo);
        }
        return appVoList;
    }

    // 문제있음
    @Override
    public List<AffinityGroupVo> getAffinitygroup(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<AffinityGroupVo> agVoList = new ArrayList<>();
        AffinityGroupVo agVo = null;

        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();
        List<AffinityGroup> affGroup =
                ((AffinityGroupsService.ListResponse)systemService.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().list().send()).groups();

        for(AffinityGroup a : affGroup){
            agVo = new AffinityGroupVo();
            System.out.println(a.name());
            agVo.setName(a.name());
            agVo.setDescription(a.description());
//            agVo.setStatus(a.); //상태 수정 필요
            agVo.setPriority(a.priority().intValue());

            agVo.setVmEnabled(a.vmsRule().enabled());
            agVo.setVmPositive(a.vmsRule().positive());
            agVo.setVmEnforcing(a.vmsRule().enforcing());

            // 수정 필요
//            if(a.positive() != null){
//                affinityGroupVO.setVmPositive(a.positive());    // 애매 a.positive()와 a.vmsRule().positive()
//            }
//            affinityGroupVO.setVmEnforcing(a.enforcing());  // 얘도 마찬가지

            agVo.setHostEnabled(a.hostsRule().enabled());
            agVo.setHostPositive(a.hostsRule().positive());
            agVo.setHostEnforcing(a.hostsRule().enforcing());

            // 수정 필요
            if(a.hostLabels().isEmpty()){
                agVo.setHostLabels("레이블 없음");
            }
            if(a.vmLabels().isEmpty()){
                agVo.setVmLabels("레이블 없음");
            }

            // 가상머신 멤버 (수정 필요)
            List<Vm> vmList =
                    ((AffinityGroupVmsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(a.id()).vmsService().list().send()).vms();
            String vmNames = "";

            for(Vm vm1 : vmList){
                vmNames += vm1.name() + " ";
            }
            agVo.setVmList(vmNames);

            // 호스트 멤버 (수정 필요)
            List<Host> hostList =
                    ((AffinityGroupHostsService.ListResponse)systemService.clustersService().clusterService(id).affinityGroupsService().groupService(a.id()).hostsService().list().send()).hosts();
            String hostNames = "";

            for(Host host : hostList){
                hostNames += host.name() + " ";
            }
            agVo.setHostList(hostNames);

            agVoList.add(agVo);
        }
        return agVoList;
    }

    @Override
    public List<AffinityLabelVo> getAffinitylabel(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<AffinityLabelVo> alVoList = new ArrayList<>();
        AffinityLabelVo alVo = null;

        List<AffinityLabel> affinityLabelList =
                ((AssignedAffinityLabelsService.ListResponse)systemService.vmsService().vmService(id).affinityLabelsService().list().send()).label();

        for(AffinityLabel a : affinityLabelList) {
            alVo = new AffinityLabelVo();
            alVo.setName(a.name());
            alVoList.add(alVo);
        }
        return alVoList;
    }

    @Override
    public GuestInfoVo getGuestInfo(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        Vm vm =
                ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();

        GuestInfoVo gif = new GuestInfoVo();

        if(vm.guestOperatingSystemPresent()){
            gif.setArchitecture(vm.guestOperatingSystem().architecture());
            System.out.println(gif.getArchitecture());
            gif.setType(vm.guestOperatingSystem().family());
            gif.setKernalVersion(vm.guestOperatingSystem().kernel().version().fullVersion());
            gif.setOs(vm.guestOperatingSystem().distribution());
            gif.setGuestTime(vm.guestTimeZone().name() + vm.guestTimeZone().utcOffset());
        }
        return gif;
    }

//    @Override
//    public vmVo getPermission(String id) {
//        return null;
//    }
//
//    @Override
//    public vmVo getEvent(String id) {
//        return null;
//    }

}
