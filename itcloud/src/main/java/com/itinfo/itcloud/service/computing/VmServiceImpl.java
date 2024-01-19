package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItVmService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// r저거 고쳐야됨 model
@Service
@Slf4j
public class VmServiceImpl implements ItVmService {

    @Autowired
    private AdminConnectionService adminConnectionService;


    @Override
    public List<VmVo> getList() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;
        Date now = new Date(System.currentTimeMillis());

        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        for(Vm vm : vmList){
            vmVo = new VmVo();

            vmVo.setStatus(vm.status().value());
            vmVo.setId(vm.id());
            vmVo.setName(vm.name());
            vmVo.setDescription(vm.description());

            // host
            if(vm.hostPresent() && vm.status().value().equals("up")) {
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

            // uptime 계산
            //  (1000*60*60*24) = 일
            //  (1000*60*60) = 시간
            //  (1000*60) = 분
            // https://192.168.0.80/ovirt-engine/api/vms/c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff/statistics  elapsed.time
            List<Statistic> statisticList =
                    ((StatisticsService.ListResponse)systemService.vmsService().vmService(vm.id()).statisticsService().list().send()).statistics();

            for(Statistic statistic : statisticList) {
                long hour = 0;
                if (statistic.name().equals("elapsed.time")) {
                    hour = statistic.values().get(0).datum().longValue() / (60*60);      //시간
                    System.out.println(vm.id() + " " +hour);

                    if(hour > 24){
                        vmVo.setUpTime(hour/24 + "일");
                    }else if( hour > 1 && hour < 24){
                        vmVo.setUpTime(hour + "시간");
                    }else {
                        vmVo.setUpTime( (statistic.values().get(0).datum().longValue() / 60) + "분");
                    }
                }
            }

            // ipv4, ipv6
            List<Nic> nicList =
                    ((VmNicsService.ListResponse) systemService.vmsService().vmService(vm.id()).nicsService().list().send()).nics();
            for (Nic nic : nicList){
                List<ReportedDevice> reportedDeviceList
                        = ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send()).reportedDevice();
                for (ReportedDevice r : reportedDeviceList){
                    vmVo.setIpv4(vm.status().value().equals("up") ? r.ips().get(0).address() : null);
                    vmVo.setIpv6(vm.status().value().equals("up") ? r.ips().get(1).address() : null);
                }
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

        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();

        vmVo.setStatus(vm.status().value());    // 상태는 두번표시됨. 그림과 글자로
        vmVo.setName(vm.name());
        vmVo.setDescription(vm.description());
        vmVo.setTemplateName( ((TemplateService.GetResponse)systemService.templatesService().templateService(vm.template().id()).get().send()).template().name() );

        if(vm.hostPresent()){
            vmVo.setHostName( ((HostService.GetResponse) systemService.hostsService().hostService(vm.host().id()).get().send()).host().name() );
        }else if(!vm.hostPresent() && vm.placementPolicy().hostsPresent()){
            vmVo.setHostName(((HostService.GetResponse) systemService.hostsService().hostService(vm.placementPolicy().hosts().get(0).id()).get().send()).host().name());
        }

        vmVo.setOsSystem(vm.os().type());
        vmVo.setChipsetFirmwareType(vm.bios().type().value());
        vmVo.setPriority(vm.highAvailability().priorityAsInteger());  // 우선순위
        vmVo.setOptimizeOption(vm.type().value());  // 최적화 옵션

        vmVo.setMemory(vm.memory());
        vmVo.setMemoryActual(vm.memoryPolicy().guaranteed());

        // 게스트os의 여유/캐시+버퍼된 메모리

        vmVo.setCpuTopologyCore(vm.cpu().topology().coresAsInteger());
        vmVo.setCpuTopologySocket(vm.cpu().topology().socketsAsInteger());
        vmVo.setCpuTopologyThread(vm.cpu().topology().threadsAsInteger());
        vmVo.setCpuCoreCnt(vmVo.getCpuTopologyCore()* vmVo.getCpuTopologySocket()* vmVo.getCpuTopologyThread());

        // 게스트 cpu수
        // 게스트 cpu
        // 고가용성

        vmVo.setMonitor(vm.display().monitorsAsInteger());      // 모니터 수
        vmVo.setUsb(vm.usb().enabled());   // usb
        // 작성자
        // 소스

        // 사용자 정의 속성
        // 클러스터 호환 버전
        vmVo.setFqdn(vm.fqdn());
        vmVo.setHwTimeOffset(vm.timeZone().name());     // 하드웨어 클럭의 시간 오프셋

//        vmVo.setStartTime(vm.startTimePresent() ? vm.startTime() : null);

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
            nVo.setPlugged(nic.plugged());    // 연결상태
            nVo.setName(nic.name());
            nVo.setLinkStatus(nic.linked());
            nVo.setType(nic.interface_().value());
            nVo.setMacAddress(nic.macPresent() ? nic.mac().address() : null);

            List<ReportedDevice> reportedDeviceList =
                    ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(id).reportedDevicesService().list().send()).reportedDevice();

            for(ReportedDevice rd : reportedDeviceList){
                nVo.setGuestInterface(rd.name());       // etho0

                if(rd.ipsPresent()){
                    String ipv6 = "";
                    nVo.setIpv4(rd.ips().get(0).address());

                    for(int i=0; i < rd.ips().size(); i++){
                        if(rd.ips().get(i).version().value().equals("v6")){
                            ipv6 += rd.ips().get(i).address()+" ";
                        }
                    }
                    nVo.setIpv6(ipv6);
                }
            }

            DecimalFormat df = new DecimalFormat("###,###");
            List<Statistic> statisticList =
                    ((StatisticsService.ListResponse)systemService.vmsService().vmService(id).nicsService().nicService(nic.id()).statisticsService().list().send()).statistics();

            for(Statistic statistic : statisticList){
                String st = "";

                if(statistic.name().equals("data.current.rx.bps")){
                    st = df.format( (statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024*1024)) );
                    nVo.setRxSpeed( st );
                }
                if(statistic.name().equals("data.current.tx.bps")){
                    st = df.format( (statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024*1024)) );
                    nVo.setTxSpeed( st );
                }
                if(statistic.name().equals("data.total.rx")){
                    st = df.format(statistic.values().get(0).datum());
                    nVo.setRxTotalSpeed( st );
                }
                if(statistic.name().equals("data.total.tx")){
                    st = df.format(statistic.values().get(0).datum());
                    nVo.setTxTotalSpeed( st );
                }
            }


            VnicProfile vnicProfile =
                    ((VnicProfileService.GetResponse)systemService.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send()).profile();
            VnicProfileVo vpVo = new VnicProfileVo();
            vpVo.setName(vnicProfile.name());   // 프로파일 이름
            vpVo.setPortMirroring(vnicProfile.portMirroring());
            nVo.setNetworkName( ((NetworkService.GetResponse)systemService.networksService().networkService(vnicProfile.network().id()).get().send()).network().name() );
            nVo.setVnicProfileVo(vpVo);

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
            vdVo.setConnection( ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm().name() );

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Snapshot> snapList =
                ((SnapshotsService.ListResponse)systemService.vmsService().vmService(id).snapshotsService().list().send()).snapshots();

        for(Snapshot snapshot : snapList){
            sVo = new SnapshotVo();

            sVo.setDate(sdf.format(snapshot.date().getTime()));
            sVo.setDescription(snapshot.description());
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

        // error
        List<AffinityGroup> affGroup =
                ((AffinityGroupsService.ListResponse)systemService.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().list().send()).groups();

        for(AffinityGroup a : affGroup){
            agVo = new AffinityGroupVo();

//            if(a.vmsPresent()){
//                a.vms().get()
//            }

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

        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();

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

    @Override
    public List<PermissionVo> getPermission(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse)systemService.vmsService().vmService(id).permissionsService().list().send()).permissions();

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = ((GroupService.GetResponse)systemService.groupsService().groupService(permission.group().id()).get().send()).get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함?

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = ((UserService.GetResponse)systemService.usersService().userService(permission.user().id()).get().send()).user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<EventVo> getEvent(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        // 2024. 1. 4. PM 04:01:21
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Event> eventList = ((EventsService.ListResponse)systemService.eventsService().list().send()).events();
        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();

        for(Event event : eventList){
            if(event.vmPresent() && event.vm().name().equals(vm.name())){
                eVo = new EventVo();

                eVo.setSeverity(event.severity().value());
                eVo.setTime(sdf.format(event.time()));
                eVo.setMessage(event.description());
                eVo.setRelationId(event.correlationIdPresent() ? event.correlationId() : "");
                eVo.setSource(event.origin());

                eVoList.add(eVo);
            }
        }
        return eVoList;
    }


}
