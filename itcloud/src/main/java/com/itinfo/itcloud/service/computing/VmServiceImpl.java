package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.VmCreateVo;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItVmService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// r저거 고쳐야됨 model
@Service
@Slf4j
public class VmServiceImpl implements ItVmService {

    @Autowired private AdminConnectionService admin;

    @Override
    public String getName(String id){
        return admin.getConnection().systemService().vmsService().vmService(id).get().send().vm().name();
    }


    @Override
    public List<VmVo> getList() {
        SystemService systemService = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;
        Date now = new Date(System.currentTimeMillis());

        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        for(Vm vm : vmList){
            Cluster cluster = systemService.clustersService().clusterService(vm.cluster().id()).get().send().cluster();
            DataCenter dataCenter = systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter();

            // uptime 계산   (1000*60*60*24) = 일  / (1000*60*60) = 시간 /  (1000*60) = 분
            // https://192.168.0.80/ovirt-engine/api/vms/c9c1c52d-d2a4-4f2a-93fe-30200f1e0bff/statistics  elapsed.time
            List<Statistic> statisticList = systemService.vmsService().vmService(vm.id()).statisticsService().list().send().statistics();

            String upTime = null;
            for(Statistic statistic : statisticList) {
                long hour = 0;
                if (statistic.name().equals("elapsed.time")) {
                    hour = statistic.values().get(0).datum().longValue() / (60*60);      // 시간

                    if(hour > 24){
                        upTime = hour/24 + "일";
                    }else if( hour > 1 && hour < 24){
                        upTime = hour + "시간";
                    }else {
                        upTime = (statistic.values().get(0).datum().longValue() / 60) + "분";
                    }
                }
            }

            // ipv4, ipv6
            List<Nic> nicList = systemService.vmsService().vmService(vm.id()).nicsService().list().send().nics();

            String ipv4 = null;
            String ipv6 = null;
            for (Nic nic : nicList){
                List<ReportedDevice> reportedDeviceList = systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();
                for (ReportedDevice r : reportedDeviceList){
                    ipv4 = vm.status().value().equals("up") ? r.ips().get(0).address() : null;
                    ipv6 = vm.status().value().equals("up") ? r.ips().get(1).address() : null;
                }
            }

            vmVo = VmVo.builder()
                    .status(vm.status().value())
                    .id(vm.id())
                    .name(vm.name())
                    .description(vm.description())
                    .hostId(vm.hostPresent() && vm.status().value().equals("up") ? vm.host().id() : null)
                    .hostName(vm.hostPresent() && vm.status().value().equals("up") ? systemService.hostsService().hostService(vm.host().id()).get().send().host().name() : null)
                    .clusterId(cluster.id())
                    .clusterName(cluster.name())
                    .datacenterId(cluster.dataCenterPresent() ? dataCenter.id() : null)
                    .datacenterName(dataCenter.name())
                    .fqdn(vm.fqdn())
                    .upTime(upTime)
                    .ipv4(ipv4)
                    .ipv6(ipv6)
                    .build();

            vmVoList.add(vmVo);
        }
        return vmVoList;
    }

    @Override
    public VmVo getInfo(String id) {
        SystemService systemService = admin.getConnection().systemService();
        Vm vm = systemService.vmsService().vmService(id).get().send().vm();

        String hostName = null;

        if(vm.hostPresent()){
            hostName = systemService.hostsService().hostService(vm.host().id()).get().send().host().name();
        }else if(!vm.hostPresent() && vm.placementPolicy().hostsPresent()){
            hostName = systemService.hostsService().hostService(vm.placementPolicy().hosts().get(0).id()).get().send().host().name();
        }

        List<Statistic> statisticList = systemService.vmsService().vmService(vm.id()).statisticsService().list().send().statistics();

        String upTime = null;
        for(Statistic statistic : statisticList) {
            long hour = 0;
            if (statistic.name().equals("elapsed.time")) {
                hour = statistic.values().get(0).datum().longValue() / (60*60);      // 시간

                if(hour > 24){
                    upTime = hour/24 + "일";
                }else if( hour > 1 && hour < 24){
                    upTime = hour + "시간";
                }else {
                    upTime = (statistic.values().get(0).datum().longValue() / 60) + "분";
                }
            }
        }

        return VmVo.builder()
                .id(id)
                .name(vm.name())
                .description(vm.description())
                .status(vm.status().value())
                .upTime(upTime)
                .templateName(systemService.templatesService().templateService(vm.template().id()).get().send().template().name())
                .hostName(hostName)
                .osSystem(vm.os().type())
                .chipsetFirmwareType(vm.bios().type().value())
                .priority(vm.highAvailability().priorityAsInteger())
                .optimizeOption(vm.type().value())
                .memory(vm.memory())
                .memoryActual(vm.memoryPolicy().guaranteed())
                // 게스트os의 여유/캐시+버퍼된 메모리
                .cpuTopologyCore(vm.cpu().topology().coresAsInteger())
                .cpuTopologySocket(vm.cpu().topology().socketsAsInteger())
                .cpuTopologyThread(vm.cpu().topology().threadsAsInteger())
                .cpuCoreCnt(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().threadsAsInteger())
                // 게스트 cpu수, 게스트 cpu, 고가용성
                .monitor(vm.display().monitorsAsInteger())
                .usb(vm.usb().enabled())
                // 작성자, 소스, 사용자 정의 속성, 클러스터 호환버전
                .fqdn(vm.fqdn())
                .hwTimeOffset(vm.timeZone().name())
                .build();
    }

    @Override
    public List<NicVo> getNic(String id) {
        SystemService systemService = admin.getConnection().systemService();

//        List<NicVo> nVoList = new ArrayList<>();
//        NicVo nVo = null;
//
//        List<Nic> nicList =
//                ((VmNicsService.ListResponse)systemService.vmsService().vmService(id).nicsService().list().send()).nics();
//
//        for(Nic nic : nicList){
//            nVo = new NicVo();
//
//            nVo.setId(nic.id());
//            nVo.setPlugged(nic.plugged());    // 연결상태
//            nVo.setName(nic.name());
//            nVo.setLinkStatus(nic.linked());
//            nVo.setType(nic.interface_().value());
//            nVo.setMacAddress(nic.macPresent() ? nic.mac().address() : null);
//
//            List<ReportedDevice> reportedDeviceList =
//                    ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(id).reportedDevicesService().list().send()).reportedDevice();
//
//            for(ReportedDevice rd : reportedDeviceList){
//                nVo.setGuestInterface(rd.name());       // etho0
//
//                if(rd.ipsPresent()){
//                    String ipv6 = "";
//                    nVo.setIpv4(rd.ips().get(0).address());
//
//                    for(int i=0; i < rd.ips().size(); i++){
//                        if(rd.ips().get(i).version().value().equals("v6")){
//                            ipv6 += rd.ips().get(i).address()+" ";
//                        }
//                    }
//                    nVo.setIpv6(ipv6);
//                }
//            }
//
//            DecimalFormat df = new DecimalFormat("###,###");
//            List<Statistic> statisticList =
//                    ((StatisticsService.ListResponse)systemService.vmsService().vmService(id).nicsService().nicService(nic.id()).statisticsService().list().send()).statistics();
//
//            for(Statistic statistic : statisticList){
//                String st = "";
//
//                if(statistic.name().equals("data.current.rx.bps")){
//                    st = df.format( (statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024*1024)) );
//                    nVo.setRxSpeed( st );
//                }
//                if(statistic.name().equals("data.current.tx.bps")){
//                    st = df.format( (statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024*1024)) );
//                    nVo.setTxSpeed( st );
//                }
//                if(statistic.name().equals("data.total.rx")){
//                    st = df.format(statistic.values().get(0).datum());
//                    nVo.setRxTotalSpeed( st );
//                }
//                if(statistic.name().equals("data.total.tx")){
//                    st = df.format(statistic.values().get(0).datum());
//                    nVo.setTxTotalSpeed( st );
//                }
//
//                if(statistic.name().equals("errors.total.rx")){
//                    st = df.format(statistic.values().get(0).datum());
//                    nVo.setStop( st );
//                }
//            }
//
//            VnicProfile vnicProfile =
//                    ((VnicProfileService.GetResponse)systemService.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send()).profile();
//            VnicProfileVo vpVo = VnicProfileVo.builder()
//                    .name(vnicProfile.name())       // 프로파일 이름
//                    .portMirroring(vnicProfile.portMirroring())
//                    .build();
//
//            nVo.setNetworkName( ((NetworkService.GetResponse)systemService.networksService().networkService(vnicProfile.network().id()).get().send()).network().name() );
//            nVo.setVnicProfileVo(vpVo);
//
//            nVoList.add(nVo);
//        }
//        return nVoList;
        return null;
    }

    @Override
    public List<VmDiskVo> getDisk(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<VmDiskVo> vdVoList = new ArrayList<>();
        VmDiskVo vdVo = null;

        List<DiskAttachment> vmdiskList =
                ((DiskAttachmentsService.ListResponse)systemService.vmsService().vmService(id).diskAttachmentsService().list().send()).attachments();
        // 별칭, 가상크기, 연결대상, 인터페이스, 논리적 이름, 상태, 유형, 설명

//        for(DiskAttachment diskAttachment : vmdiskList){
//            vdVo = new VmDiskVo();
//
//            vdVo.setId(diskAttachment.id());
//            vdVo.setActive(diskAttachment.active());
//            vdVo.setReadOnly(diskAttachment.readOnly());
//            vdVo.setBootAble(diskAttachment.bootable());
//            vdVo.setLogicalName(diskAttachment.logicalName());
//            vdVo.setInterfaceName(diskAttachment.interface_().value());
//
//            Disk disk =
//                    ((DiskService.GetResponse)systemService.disksService().diskService(diskAttachment.disk().id()).get().send()).disk();
//            vdVo.setName(disk.name());
//            vdVo.setDescription(disk.description());
//            vdVo.setVirtualSize(disk.provisionedSize());
//            vdVo.setStatus(disk.status().value());  // 유형
//            vdVo.setType(disk.storageType().value());
//            vdVo.setConnection( ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm().name() );
//
//            vdVoList.add(vdVo);
//        }
        return vdVoList;
    }

//    https://192.168.0.80/ovirt-engine/api/vms/931ad1d3-0782-4727-947d-6a765cfcc401/snapshots
    @Override
    public List<SnapshotVo> getSnapshot(String id) {
        SystemService systemService = admin.getConnection().systemService();

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
        SystemService systemService = admin.getConnection().systemService();

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
        SystemService systemService = admin.getConnection().systemService();

//        List<AffinityGroupVo> agVoList = new ArrayList<>();
//        AffinityGroupVo agVo = null;
//
//        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();
//
//        // error
//        List<AffinityGroup> affGroup =
//                ((AffinityGroupsService.ListResponse)systemService.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().list().send()).groups();
//
//        for(AffinityGroup a : affGroup){
//            agVo = new AffinityGroupVo();
//
//            agVo.setName(a.name());
//            agVo.setDescription(a.description());
//            agVo.setStatus(a.broken());
//            agVo.setPriority(a.priority().intValue());
//
//            agVo.setPositive(a.positivePresent() && a.positive());
//
//            agVo.setVmEnabled(a.vmsRule().enabled());
//            agVo.setVmPositive(a.vmsRule().positive());
//            agVo.setVmEnforcing(a.vmsRule().enforcing());
//
//            agVo.setHostEnabled(a.hostsRule().enabled());
//            agVo.setHostPositive(a.hostsRule().positive());
//            agVo.setHostEnforcing(a.hostsRule().enforcing());
//
//            // 가상머신 멤버 (수정 필요)
//            List<Vm> vmList =
//                    ((AffinityGroupVmsService.ListResponse)systemService.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().groupService(a.id()).vmsService().list().send()).vms();
//            List<String> vmNames = new ArrayList<>();
//            for (Vm vms : vmList){
//                vmNames.add(vms.name());
//            }
//            agVo.setVmList(vmNames);
//
//            // 호스트 멤버 (수정 필요)
//            List<Host> hostList = systemService.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().groupService(a.id()).hostsService().list().send().hosts();
//            List<String> hostNames = new ArrayList<>();
//            for(Host host : hostList){
//                hostNames.add(host.name());
//            }
//            agVo.setHostList(hostNames);
//
//
//            // 가상머신 레이블
//            List<AffinityLabel> vmLabel = systemService.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().groupService(a.id()).vmLabelsService().list().send().labels();
//            List<String> vms = new ArrayList<>();
//            for(AffinityLabel affinityLabel : vmLabel) {
//                if(affinityLabel != null){
//                    vms.add(affinityLabel.name());
//                }
//            }
//            agVo.setVmLabels(vms);
//
//            // 호스트 레이블
//            List<AffinityLabel> hostLabel = systemService.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().groupService(a.id()).hostLabelsService().list().send().labels();
//            List<String> hosts = new ArrayList<>();
//            for(AffinityLabel affinityLabel : hostLabel){
//                if(affinityLabel != null){
//                    hosts.add(affinityLabel.name());
//                }
//            }
//            agVo.setHostLabels(hosts);
//
//            agVoList.add(agVo);
//        }
//        return agVoList;
        return null;
    }

    @Override
    public List<AffinityLabelVo> getAffinitylabel(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<AffinityLabelVo> alVoList = new ArrayList<>();
        AffinityLabelVo alVo = null;

        List<AffinityLabel> affinityLabelList = systemService.affinityLabelsService().list().send().labels();
        for(AffinityLabel affinityLabel : affinityLabelList){
            // 호스트
            List<Host> hostList = systemService.affinityLabelsService().labelService(affinityLabel.id()).hostsService().list().send().hosts();
            List<String> hosts = new ArrayList<>();
            for(Host host : hostList){
                hosts.add(getName(host.id()));
            }

            // 가상머신
            List<Vm> vmList = systemService.affinityLabelsService().labelService(affinityLabel.id()).vmsService().list().send().vms();
            List<String> vms = new ArrayList<>();
            for(Vm vm : vmList){
                vms.add(getName(vm.id()));
            }

            alVo = AffinityLabelVo.builder()
                    .id(affinityLabel.id())
                    .name(affinityLabel.name())
//                    .hosts(hosts)
//                    .vms(vms)
                    .build();

            alVoList.add(alVo);
        }
        return alVoList;
    }

    @Override
    public GuestInfoVo getGuestInfo(String id) {
        SystemService systemService = admin.getConnection().systemService();

        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();

        GuestInfoVo gif = new GuestInfoVo();

        if(vm.guestOperatingSystemPresent()){
            gif.setArchitecture(vm.guestOperatingSystem().architecture());
            System.out.println(gif.getArchitecture());
            gif.setType(vm.guestOperatingSystem().family());
            gif.setKernalVersion(vm.guestOperatingSystem().kernel().version().fullVersion());
            gif.setOs(vm.guestOperatingSystem().distribution() + " " + vm.guestOperatingSystem().version().major());
            gif.setGuestTime(vm.guestTimeZone().name() + vm.guestTimeZone().utcOffset());
        }
        return gif;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

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
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        // 2024. 1. 4. PM 04:01:21
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Event> eventList = ((EventsService.ListResponse)systemService.eventsService().list().send()).events();
        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(id).get().send()).vm();

        for(Event event : eventList){
            if(event.vmPresent() && event.vm().name().equals(vm.name())){
                eVo = EventVo.builder()
                        .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                        .time(sdf.format(event.time()))
                        .message(event.description())
                        .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                        .source(event.origin())
                        .build();

                eVoList.add(eVo);
            }
        }
        return eVoList;
    }


    // vm cluster list
    @Override
    public List<ClusterVo> getClusterList() {
        SystemService systemService = admin.getConnection().systemService();

        List<ClusterVo> clusterVoList = new ArrayList<>();
        ClusterVo cVo = null;

        List<Cluster> clusterList = systemService.clustersService().list().send().clusters();
        for(Cluster cluster : clusterList){
            cVo = ClusterVo.builder()
                    .id(cluster.id())
                    .name(cluster.name())
                    .datacenterName(systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                    .build();

            clusterVoList.add(cVo);
        }
        log.info("clusterList");
        return clusterVoList;
    }

    @Override
    public VmCreateVo getVmCreate(String id) {
        SystemService systemService = admin.getConnection().systemService();

        Vm vm = systemService.vmsService().vmService(id).get().send().vm();
        Cluster cluster = systemService.clustersService().clusterService(vm.cluster().id()).get().send().cluster();
        String dcName = systemService.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name();

        log.info("getVmCreate");

//        return VmCreateVo.builder()
//                .clusterId(cluster.id())
//                .clusterName(cluster.name())
//                .datacenterName(dcName)
//                .templateId(vm.templatePresent() ? vm.template().id() : null)
//                .templateName(vm.templatePresent() ? systemService.templatesService().templateService(vm.template().id()).get().send().template().name() : null)
//                .os(vm.os.type)
//        https://ovirt.github.io/ovirt-engine-api-model/master/#types/os_type
//                .id(id)      // vm id
//                .name(vm.name())
//                .description(vm.description())
//                .comment(vm.comment())
//                .statusSave(vm.stateless()) // 상태 비저장 (확실치 않음)
//                .startPaused(vm.startPaused())
//                .deleteProtected(vm.deleteProtected())
////                .vDiskVo()    // 이미지 or 직접 lun or 관리되는 블록
////                .vnicList()   // vnic 프로파일
//                .vmSystemVo( VmSystemVo.builder()
//                                    .memorySize(vm.memory())
//                                    .memoryMax(vm.memoryPolicy().max())
//                                    .memoryActual(vm.memoryPolicy().guaranteed())
//                                    .vCpuCnt(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().threadsAsInteger())
//                                    .vCpuSocket(vm.cpu().topology().socketsAsInteger())
//                                    .vCpuSocketCore(vm.cpu().topology().coresAsInteger())
//                                    .vCpuCoreThread(vm.cpu().topology().threadsAsInteger())
//                                    .userEmulation()
//                                    .userCpu()
//                                    .userVersion()
//                                    .instanceType(vm.instanceTypePresent() ? vm.instanceType().name() : null)
//                                    .timeOffset()
//                                    .serialNumPolicy()
//                                    .userSerialNum()
//                                    .build() )
//                .vmHostVo( VmHostVo.builder()
//                                    .hostCpuPass()
//                                    .tsc()
//                                    .migrationMode()
//                                    .migrationPolicy()
//                                    .migrationEncoding()
//                                    .parallelMigration()
//                                    .numOfVmMigration()
//                                    .numaNode()
//                                    .build() )
//                .vmHaVo( VmHaVo.builder()
//                                    .ha()
//                                    .vmStorageDomain()
//                                    .resumeOperation()
//                                    .priority()
//                                    .watchDogModel()
//                                    .watchDogWork()
//                                    .build() )
//                .vmResourceVo( VmResourceVo.builder()
//                                    .cpuProfile()
//                                    .cpuShare()
//                                    .cpuPinningPolicy()
//                                    .cpuPinningTopology()
//                                    .memoryBalloon()
//                                    .tpm()
//                                    .ioThread()
//                                    .ioThreadCnt()
//                                    .multiQue()
//                                    .virtioScsi()
//                                    .virtioScsiQueues()
//                                    .build() )
//                .vmBootVo( VmBootVo.builder()
//                                    .firstDevice()
//                                    .secondDevice()
//                                    .cdDvdConn()
//                                    .bootingMenu()
//                                    .build() )
////                .affinityGroupVoList()
////                .affinityLabelVoList()
//                .build();
        return null;
    }

    @Override
    public boolean addVm(VmCreateVo vmCreateVo) {
        SystemService systemService = admin.getConnection().systemService();
        VmsService vmsService = systemService.vmsService();
        List<Vm> vmList = systemService.vmsService().list().send().vms();

        try {
            Vm vm = null;

            vm = new VmBuilder()
                    .cluster(new ClusterBuilder().id(vmCreateVo.getClusterId()))
                    .template(new TemplateBuilder().id(vmCreateVo.getTemplateId()))
                    .os(new OperatingSystemBuilder().type(vmCreateVo.getOs()))
                    .bios(new BiosBuilder().type(BiosType.valueOf(vmCreateVo.getChipsetType())))
                    .type(VmType.valueOf(vmCreateVo.getOption()))   // 최적화 옵션
                    .name(vmCreateVo.getName())
                    .description(vmCreateVo.getDescription())
                    .comment(vmCreateVo.getComment())
                    .stateless(vmCreateVo.isStatusSave())
                    .startPaused(vmCreateVo.isStartPaused())
                    .deleteProtected(vmCreateVo.isDeleteProtected())
//                    .diskAttachments(vmCreateVo.getVDiskVo())
//                    .nics(vmCreateVo.getVnicList())

                    .memory(vmCreateVo.getVmSystemVo().getMemorySize())
                    .memoryPolicy(new MemoryPolicyBuilder()
                                        .max(vmCreateVo.getVmSystemVo().getMemoryMax())
                                        .guaranteed(vmCreateVo.getVmSystemVo().getMemoryActual()))
                    .cpu(new CpuBuilder()
                                    .topology(new CpuTopologyBuilder()
                                        .cores(vmCreateVo.getVmSystemVo().getVCpuSocketCore())
                                            .sockets(vmCreateVo.getVmSystemVo().getVCpuSocket())
                                            .threads(vmCreateVo.getVmSystemVo().getVCpuCoreThread())))
                    // 사용자 정의 에뮬레이션 부분 보류
//                    .instanceType(new InstanceTypeBuilder().type())
                    .timeZone(new TimeZoneBuilder().name(vmCreateVo.getVmSystemVo().getTimeOffset()))
                    // 일련 번호 정책
                    // 사용자 정의 일련번호
//                    .host(new HostBuilder().)  //호스트
                    .migration(new MigrationOptionsBuilder()
                                    .autoConverge(InheritableBoolean.valueOf(vmCreateVo.getVmHostVo().getMigrationMode()))
                            .encrypted(InheritableBoolean.valueOf(vmCreateVo.getVmHostVo().getMigrationEncoding()))
                    )
//                    .migrationDowntime(vmCreateVo.getVmHostVo().getMigrationPolicy())
                    .build();

            log.info("----" + vmCreateVo.toString());


        }catch (Exception e){

        }


        return false;
    }

    @Override
    public void editVm(VmCreateVo vmCreateVo) {
        SystemService systemService = admin.getConnection().systemService();

    }

    @Override
    public boolean deleteVm(String id) {
        SystemService systemService = admin.getConnection().systemService();

        return false;
    }
}
