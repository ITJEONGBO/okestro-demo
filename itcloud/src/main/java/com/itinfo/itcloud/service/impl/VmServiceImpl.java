package com.itinfo.itcloud.service.impl;

import com.google.gson.Gson;
import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.OsVo;
import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.*;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItVmService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.services.VmService;
import org.ovirt.engine.sdk4.services.VmsService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;


// r저거 고쳐야됨 model
@Service
@Slf4j
public class VmServiceImpl implements ItVmService {
    @Autowired private AdminConnectionService admin;
    @Autowired private CommonService commonService;



    // 가상머신 목록
    @Override
    public List<VmVo> getList() {
        SystemService system = admin.getConnection().systemService();
        List<Vm> vmList = system.vmsService().list().send().vms();

        log.info("가상머신 리스트");
        return vmList.stream()
                .map(vm -> {
                    Cluster cluster = system.clustersService().clusterService(vm.cluster().id()).get().send().cluster();
                    DataCenter dataCenter = system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter();

                    return VmVo.builder()
                            .status(TypeExtKt.findVmStatus(vm.status()))
                            // 엔진여부
                            .name(vm.name())
                            .hostId(vm.hostPresent() ? vm.host().id() : null)
                            .hostName(vm.hostPresent() ? system.hostsService().hostService(vm.host().id()).get().send().host().name() : null)
                            .ipv4(commonService.getVmIp(system, vm.id(), "v4"))
                            .ipv6(commonService.getVmIp(system, vm.id(), "v6"))
                            .fqdn(vm.fqdn())
                            .clusterId(cluster.id())
                            .clusterName(cluster.name())
                            .datacenterId(dataCenter.id())
                            .datacenterName(dataCenter.name())
                            // 메모리, cpu, 네트워크
                            .upTime(commonService.getVmUptime(system, vm.id()))
                            .description(vm.description())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<VmSetVo> setVmSet() {
        SystemService system = admin.getConnection().systemService();
        List<Cluster> clusterList = system.clustersService().list().send().clusters();
        List<Host> hostList = system.hostsService().list().send().hosts();

        return clusterList.stream()
                .filter(cluster -> cluster.dataCenterPresent() && cluster.cpuPresent())
                .map(cluster -> {
                    List<Network> networkList = system.clustersService().clusterService(cluster.id()).networksService().list().send().networks();
                    List<CpuProfile> profileList = system.clustersService().clusterService(cluster.id()).cpuProfilesService().list().send().profiles();
                    List<AffinityGroup> affinityGroupList = system.clustersService().clusterService(cluster.id()).affinityGroupsService().list().send().groups();
                    List<AffinityLabel> affinityLabelList = system.affinityLabelsService().list().send().labels();

                    return VmSetVo.builder()
                            .clusterId(cluster.id())
                            .clusterName(cluster.name())
                            .dcName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                            .vnicList(
                                    networkList.stream()
                                            .flatMap(network -> {
                                                List<VnicProfile> vnicProfileList = system.networksService().networkService(network.id()).vnicProfilesService().list().send().profiles();
                                                return vnicProfileList.stream()
                                                        .map(vnicProfile ->
                                                                VmVnicVo.builder()
                                                                    .id(vnicProfile.id())
                                                                    .name(vnicProfile.name())
                                                                    .networkName(network.name())
                                                                    .externalNetwork(network.externalProviderPresent())
                                                                    .build()
                                                        );
                                            })
                                            .collect(Collectors.toList())
                            )
                            .profileVoList(
                                    profileList.stream()
                                            .map(cpuProfile ->
                                                    IdentifiedVo.builder()
                                                            .id(cpuProfile.id())
                                                            .name(cpuProfile.name())
                                                            .build()
                                            )
                                            .collect(Collectors.toList())
                            )
                            .hostVoList(
                                    hostList.stream()
                                            .filter(host -> host.cluster().id().equals(cluster.id()))
                                            .map(host ->
                                                IdentifiedVo.builder()
                                                        .id(host.id())
                                                        .name(host.name())
                                                        .build()
                                            )
                                            .collect(Collectors.toList())
                            )
                            .agVoList(
                                    affinityGroupList.stream()
                                            .map(affinityGroup ->
                                                    IdentifiedVo.builder()
                                                            .id(affinityGroup.id())
                                                            .name(affinityGroup.name())
                                                            .build()
                                            )
                                            .collect(Collectors.toList())
                            )
                            .alVoList(
                                    affinityLabelList.stream()
                                            .map(affinityLabel ->
                                                    IdentifiedVo.builder()
                                                            .id(affinityLabel.id())
                                                            .name(affinityLabel.name())
                                                            .build()
                                            )
                                            .collect(Collectors.toList())
                            )
                            .build();
                })
                .collect(Collectors.toList());

//                                profileList.stream()
//                                    .map(cpuProfile ->
//                                        CpuProfileVo.builder()
//                                                .id(cpuProfile.id())
//                                                .name(cpuProfile.name())
//                                                .build()
//                                    )
//                                    .collect(Collectors.toList())
//                                hostList.stream()
//                                    .filter(host -> host.cluster().id().equals(cluster.id()))
//                                    .map(host ->
//                                            HostVo.builder()
//                                                .id(host.id())
//                                                .name(host.name())
//                                                .build()
//                                    )
//                                    .collect(Collectors.toList())
//                                affinityGroupList.stream()
//                                    .map(ag ->
//                                            AffinityGroupVo.builder()
//                                                .id(ag.id())
//                                                .name(ag.name())
//                                                .build()
//                                    )
//                                    .collect(Collectors.toList())
//                                affinityLabelList.stream()
//                                    .map(al ->
//                                            AffinityLabelVo.builder()
//                                                .id(al.id())
//                                                .name(al.name())
//                                                .build()
//                                    )
//                                    .collect(Collectors.toList())
    }

    // 가상머신 생성
    @Override
    public CommonVo<Boolean> addVm(VmCreateVo vmVo) {
        SystemService system = admin.getConnection().systemService();
        VmsService vmsService = system.vmsService();
        List<Host> hostList = system.hostsService().list().send().hosts();

        BigInteger convertMb = BigInteger.valueOf(1024).pow(2);
        boolean duplicateName = vmsService.list().search("name=" + vmVo.getName()).send().vms().isEmpty();

        Gson gson = new Gson();

        try {
            if(duplicateName) { // 이름 중복 검사
                VmBuilder vmBuilder = new VmBuilder();
                vmBuilder
                        .cluster(new ClusterBuilder().id(vmVo.getClusterId()))
                        .template(new TemplateBuilder().id(vmVo.getTemplateId()))
                        .os(new OperatingSystemBuilder().type(vmVo.getOs()).build())  // 운영시스템
                        .bios(new BiosBuilder().type(BiosType.valueOf(vmVo.getChipsetType())).build()) // 칩셋
                        .type(VmType.valueOf(vmVo.getOption()))   // 최적화 옵션

                        .name(vmVo.getName())
                        .description(vmVo.getDescription())
                        .comment(vmVo.getComment())
                        .stateless(vmVo.isStateless())      // 상태 비저장
                        .startPaused(vmVo.isStartPaused())  // 일시정지
                        .deleteProtected(vmVo.isDeleteProtected()); // 삭제 방지

//                    .diskAttachments(vmCreateVo.getVDiskVo()) // 인스턴스 이미지
//                    .nics(vmCreateVo.getVnicList())           // vnic profile


                // 시스템
                // 인스턴스 유형에 따라 메모리와 가상 cpu값이 바뀐다
                // 빈값이 아니면 인스턴스 유형 값을 대입
                if (!"".equals(vmVo.getVmSystemVo().getInstanceType())) {
                    InstanceType it = system.instanceTypesService().list().search("name=" + vmVo.getVmSystemVo().getInstanceType()).send().instanceType().get(0);

                    vmBuilder
                            .memory(it.memory())
                            .memoryPolicy(
                                    new MemoryPolicyBuilder()
                                            .max(it.memoryPolicy().max())
                                            .ballooning(true)   // 리소스할당- 메모리 balloon 활성화
                                            .guaranteed(it.memoryPolicy().guaranteed())
                            )
                            .cpu(
                                    new CpuBuilder()
                                            .topology(
                                                    new CpuTopologyBuilder()
                                                            .cores(it.cpu().topology().coresAsInteger())
                                                            .sockets(it.cpu().topology().socketsAsInteger())
                                                            .threads(it.cpu().topology().threadsAsInteger())
                                            )
                            )
                            .instanceType(it);
                } else {    // 사용자 정의 값
                    vmBuilder
                            .memory(BigInteger.valueOf(vmVo.getVmSystemVo().getMemorySize()).multiply(convertMb))
                            .memoryPolicy(
                                    new MemoryPolicyBuilder()
                                            .max(BigInteger.valueOf(vmVo.getVmSystemVo().getMemoryMax()).multiply(convertMb))
                                            .ballooning(vmVo.getVmResourceVo().isMemoryBalloon())   // 리소스할당- 메모리 balloon 활성화
                                            .guaranteed(BigInteger.valueOf(vmVo.getVmSystemVo().getMemoryActual()).multiply(convertMb))
                            )
                            .cpu(
                                    new CpuBuilder()
                                            .topology(
                                                    new CpuTopologyBuilder()
                                                            .cores(vmVo.getVmSystemVo().getVCpuSocketCore())
                                                            .sockets(vmVo.getVmSystemVo().getVCpuSocket())
                                                            .threads(vmVo.getVmSystemVo().getVCpuCoreThread())
                                            )
                            );
                }

                // 초기 실행
                // ???
//                if (vmVo.getVmInitVo().isCloudInit()) {
//                    vmBuilder
//                            .initialization(
//                                    new InitializationBuilder()
//                                            .hostName(vmVo.getVmInitVo().getHostName())
//                                            .timezone(vmVo.getVmInitVo().getTimeStandard())
//                            );
//                }


                // 호스트
                // 마이그레이션은 건들 필요 없는데 parallel
                // TODO 마이그레이션, numa 여쭤보기
                VmPlacementPolicyBuilder placementBuilder = new VmPlacementPolicyBuilder();
                if(!vmVo.getVmHostVo().isClusterHost()){
                    placementBuilder.hosts(new HostBuilder().id(vmVo.getVmHostVo().getSelectHostId()));
                }
                vmBuilder.placementPolicy(
                        placementBuilder
                                .affinity("".equals(vmVo.getVmSystemVo().getInstanceType()) ?
                                        VmAffinity.valueOf(vmVo.getVmHostVo().getMigrationMode())
                                        : VmAffinity.MIGRATABLE
                                )
                );

//                vmBuilder.migration(
//                        new MigrationOptionsBuilder().build()
//                );


                // 고가용성
                vmBuilder.highAvailability(
                        new HighAvailabilityBuilder()
                                .enabled(vmVo.getVmHaVo().isHa()) // 고가용성 여부
                                .priority(vmVo.getVmHaVo().getPriority())
                                // 재개동작 모르겠음
                );

                if(vmVo.getVmHaVo().isHa()) {       // 스토리지 도메인 지정
                    vmBuilder.lease(
                            new StorageDomainLeaseBuilder()
                                    .storageDomain(
                                            new StorageDomainBuilder().id(vmVo.getVmHaVo().getVmStorageDomainId())
                                    )
                    );
                }

//                if(!"".equals(vmVo.getVmHaVo().getWatchDogModel())){  // 워치독 필요한가?
//                    System.out.println("watchdog");
//                    vmBuilder.watchdogs(
//                            new WatchdogBuilder()
                                    // 만들어질 vm의 id값을 넣어야됨
//                                    .vm(new VmBuilder().id())
//                                    .model(WatchdogModel.valueOf(vmVo.getVmHaVo().getWatchDogModel()))  // I6300ESB
//                                    .action(WatchdogAction.valueOf(vmVo.getVmHaVo().getWatchDogAction()))
//                    );
//                }




                // 리소스 할당
                // CPU 할당
                // ovirt 생성하고 내꺼 생성하고 템플릿 때문에 리소스할당에 ㄸ느느 스토리지할당 메뉴가 다름
//                vmBuilder
//                        .autoPinningPolicy("RESIZE_AND_PIN_NUMA".equals(vmVo.getVmResourceVo().getCpuPinningPolicy()) ? AutoPinningPolicy.ADJUST : AutoPinningPolicy.DISABLED)
//                        .cpuPinningPolicy(CpuPinningPolicy.valueOf(vmVo.getVmResourceVo().getCpuPinningPolicy()))
//                        .cpuShares(vmVo.getVmResourceVo().getCpuShare());
//
//                vmBuilder
//                        .virtioScsiMultiQueuesEnabled(vmVo.getVmResourceVo().isMultiQue());








//            vmBuilder
//                .os(
//                        new OperatingSystemBuilder()
//                                .boot(new BootBuilder().devices(BootDevice.valueOf("hd")))
//                );


                // 사용자 정의 에뮬레이션 부분 보류
                // 하드웨어 클럭의 시간 오프셋
                vmBuilder
                        .timeZone(
                                new TimeZoneBuilder().name(vmVo.getVmSystemVo().getTimeOffset())
                        );
                // 일련 번호 정책
                // 사용자 정의 일련번호


                System.out.println(gson.toJson(vmBuilder));
                System.out.println(vmVo.toString());

                Vm vm1 = vmsService.add().vm(vmBuilder.build()).send().vm();

                log.info("가상머신 생성 완료");
                return CommonVo.createResponse();
            }else{
                log.error("가상머신 이름 중복");
                return CommonVo.duplicateResponse();    
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("가상머신 생성실패");
            return CommonVo.failResponse("가상머신 생성 실패");
        }
    }



    // 가상머신 편집 창
    @Override
    public VmCreateVo setEditVm(String id) {
        SystemService system = admin.getConnection().systemService();
        Vm vm = system.vmsService().vmService(id).get().send().vm();
        Cluster cluster = system.clustersService().clusterService(vm.cluster().id()).get().send().cluster();
//        List<OperatingSystemInfo> osList = system.operatingSystemsService().list().send().operatingSystem();
        List<DiskAttachment> daList = system.vmsService().vmService(id).diskAttachmentsService().list().send().attachments();

        BigInteger convertMb = BigInteger.valueOf(1024).pow(2);

        log.info("가상머신 편집 창");

        return VmCreateVo.builder()
                .dcName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                .clusterId(cluster.id())
                .clusterName(cluster.name())
                .templateId(vm.template().id()) // 출력만 가능
                .templateName(system.templatesService().templateService(vm.template().id()).get().send().template().name())
                .os(TypeExtKt.findOs(OsVo.valueOf(vm.os().type())))
                .chipsetType(TypeExtKt.findBios(vm.bios().type()))
                .option(TypeExtKt.findVmType(vm.type()))
                
                .id(id)      // vm id
                .name(vm.name())
                .description(vm.description())
                .comment(vm.comment())
                .stateless(vm.stateless()) // 상태 비저장 (확실치 않음)
                .startPaused(vm.startPaused())
                .deleteProtected(vm.deleteProtected())
                .vDiskList(
                        daList.stream()
                                .map(diskAttachment -> {
                                    return VDiskVo.builder()
                                            .build();
                                })
                                .collect(Collectors.toList())
                )    // 이미지 or 직접 lun or 관리되는 블록
//                .vnicList()   // vnic 프로파일 vm.memory()
                .vmSystemVo(
                        VmSystemVo.builder()
                                .memorySize(vm.memory().divide(convertMb).longValue())
                                .memoryMax(vm.memoryPolicy().max().divide(convertMb).longValue())
                                .memoryActual(vm.memoryPolicy().guaranteed().divide(convertMb).longValue())
                                .vCpuSocket(vm.cpu().topology().socketsAsInteger())
                                .vCpuSocketCore(vm.cpu().topology().coresAsInteger())
                                .vCpuCoreThread(vm.cpu().topology().threadsAsInteger())
                                .vCpuCnt(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().threadsAsInteger())
                                .instanceType(vm.instanceTypePresent() ? system.instanceTypesService().instanceTypeService(vm.instanceType().id()).get().send().instanceType().name() : null)
                                .timeOffset(vm.timeZone().utcOffset()) // 선택: gmtz, kst
//                                    .userEmulation()  // 사용자 정의 에뮬레이션 시스템
//                                    .userCpu()        // 사용자 정의 cpu
//                                    .userVersion()    // 사용자 정의 호환버전
//                                    .serialNumPolicy()// 일련번호 정책 (기본: 클러스터 기본값(HostId))
//                                    .userSerialNum()  // 사용자 정의 일련 번호
                                .build()
                )
                .vmHostVo(
                        VmHostVo.builder()
//                                    .hostCpuPass()
//                                    .tsc()
//                                    .migrationMode()
//                                    .migrationPolicy()
//                                    .migrationEncoding()
//                                    .parallelMigration()
//                                    .numOfVmMigration()
//                                    .numaNode()
                                .build()
                )
                .vmHaVo(
                        VmHaVo.builder()
//                                    .ha()
//                                    .vmStorageDomain()
//                                    .resumeOperation()
//                                    .priority()
//                                    .watchDogModel()
//                                    .watchDogWork()
                                .build()
                )
                .vmResourceVo(
                        VmResourceVo.builder()
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
                                .build()
                )
                .vmBootVo(
                        VmBootVo.builder()
//                                    .firstDevice()
//                                    .secondDevice()
//                                    .cdDvdConn()
//                                    .bootingMenu()
                                .build()
                )
//                .affinityGroupVoList()
//                .affinityLabelVoList()
                .build();
    }


    // 가상머신 편집
    @Override
    public CommonVo<Boolean> editVm(String id, VmCreateVo vmCreateVo) {
        SystemService system = admin.getConnection().systemService();



        return null;
    }

    // 가상머신 삭제
    @Override
    public CommonVo<Boolean> deleteVm(String id) {
        SystemService system = admin.getConnection().systemService();
        VmService vmService = system.vmsService().vmService(id);
        boolean delete = system.vmsService().vmService(id).get().send().vm().deleteProtected();

        try {
            if(!delete){
                vmService.remove().detachOnly(true /*여기에 디스크 삭제여부*/).send();
                log.info("가상머신 삭제 성공");
                return CommonVo.successResponse();
            }else {
                log.error("삭제방지 모드를 해제하세요");
                return CommonVo.failResponse("삭제방지 모드를 해제");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return CommonVo.failResponse("가상머신 삭제 실패");
        }
    }




    // 일반09
    @Override
    public VmVo getInfo(String id) {
        SystemService system = admin.getConnection().systemService();
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        String hostName = null;
        if(vm.hostPresent()){
            hostName = system.hostsService().hostService(vm.host().id()).get().send().host().name();
        }else if(!vm.hostPresent() && vm.placementPolicy().hostsPresent()){
            hostName = system.hostsService().hostService(vm.placementPolicy().hosts().get(0).id()).get().send().host().name();
        }

        return VmVo.builder()
                .id(id)
                .name(vm.name())
                .description(vm.description())
                .status(TypeExtKt.findVmStatus(vm.status()))
                .upTime(commonService.getVmUptime(system, vm.id()))
                .templateName(system.templatesService().templateService(vm.template().id()).get().send().template().name())
                .hostName(hostName)
                .osSystem(vm.os().type())
                .chipsetFirmwareType(TypeExtKt.findBios(vm.bios().type()))
                .priority(vm.highAvailability().priorityAsInteger())
                .optimizeOption(TypeExtKt.findVmType(vm.type()))
                .memory(vm.memory())
                .memoryActual(vm.memoryPolicy().guaranteed())
                // 게스트os의 여유/캐시+버퍼된 메모리
//                .guestBufferedMemory() // memory.free
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

    // 네트워크 인터페이스
    @Override
    public List<NicVo> getNic(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();

        return nicList.stream()
                .map(nic -> {
                    List<Statistic> statisticList = system.vmsService().vmService(id).nicsService().nicService(nic.id()).statisticsService().list().send().statistics();
                    VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();

                    return NicVo.builder()
                            .id(nic.id())
                            .plugged(nic.plugged())  // 연결상태
                            .networkName(system.networksService().networkService(vnicProfile.network().id()).get().send().network().name())
                            .vnicProfileVo(
                                VnicProfileVo.builder()
                                    .name(vnicProfile.name())       // 프로파일 이름
                                    .portMirroring(vnicProfile.portMirroring())
                                .build()
                            )
                            .name(nic.name())
                            .linkStatus(nic.linked())
                            .type(nic.interface_().value())
                            .macAddress(nic.macPresent() ? nic.mac().address() : null)
                            .ipv4(commonService.getVmIp(system, id, "v4"))
                            .ipv6(commonService.getVmIp(system, id, "v6"))
                            .rxSpeed(commonService.getSpeed(statisticList, "data.current.rx.bps"))
                            .txSpeed(commonService.getSpeed(statisticList, "data.current.tx.bps"))
                            .rxTotalSpeed(commonService.getSpeed(statisticList, "data.total.rx"))
                            .txTotalSpeed(commonService.getSpeed(statisticList, "data.total.tx"))
                            .stop(commonService.getSpeed(statisticList, "errors.total.rx"))
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 디스크
    // 별칭, 가상크기, 연결대상, 인터페이스, 논리적 이름, 상태, 유형, 설명
    @Override
    public List<VmDiskVo> getDisk(String id) {
        SystemService system = admin.getConnection().systemService();
        List<DiskAttachment> diskList = system.vmsService().vmService(id).diskAttachmentsService().list().send().attachments();

        return diskList.stream()
                .map(diskAttachment -> {
                    Disk disk = system.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
                    return VmDiskVo.builder()
                            .id(diskAttachment.id())
                            .active(diskAttachment.active())
                            .readOnly(diskAttachment.readOnly())
                            .bootAble(diskAttachment.bootable())
                            .logicalName(diskAttachment.logicalName())
                            .interfaceName(diskAttachment.interface_().value())
                            .name(disk.name())
                            .description(disk.description())
                            .virtualSize(disk.provisionedSize())
                            .status(disk.status().value())
                            .type(TypeExtKt.findStorageType(disk.storageType()))
                            .connection(system.vmsService().vmService(id).get().send().vm().name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // TODO
    // 스냅샷
    @Override
    public List<SnapshotVo> getSnapshot(String id) {
        SystemService system = admin.getConnection().systemService();
        Vm vm = system.vmsService().vmService(id).get().send().vm();
        List<Snapshot> snapList = system.vmsService().vmService(id).snapshotsService().list().send().snapshots();
        List<DiskAttachment> daList = system.vmsService().vmService(id).diskAttachmentsService().list().send().attachments();

        daList.stream().map(diskAttachment -> diskAttachment.disk().id()).forEach(System.out::println);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        return snapList.stream()
                .map(snapshot -> {
                    if(snapshot.vmPresent()) {  // 스냅샷 생성
                        List<Disk> diskList = system.vmsService().vmService(id).snapshotsService().snapshotService(snapshot.id()).disksService().list().send().disks();
                        List<Nic> nicList = system.vmsService().vmService(id).snapshotsService().snapshotService(snapshot.id()).nicsService().list().send().nics();

                        return SnapshotVo.builder()
                                .id(snapshot.id())
                                .description(snapshot.description())
                                .date(sdf.format(snapshot.date().getTime()))
                                .status(snapshot.snapshotStatus().value())
                                .persistMemorystate(snapshot.persistMemorystate())
                                .setMemory(snapshot.vm().memory())
                                .guaranteedMemory(snapshot.vm().memoryPolicy().guaranteed())
                                .cpuCore(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().threadsAsInteger())
                                .sDiskList(
                                        diskList.stream()
                                                .map(disk -> {
                                                    return SnapshotDiskVo.builder()
                                                            .status(disk.status())
                                                            .id(disk.id())
                                                            .alias(disk.alias())
                                                            .virtualSize(disk.provisionedSize())
                                                            .actualSize(disk.actualSize())
                                                            .sparse(disk.sparse() ? "sparse" : "?")
//                                                                    .diskInterface(diskAttachment.interface_())
//                                                        .date()
//                                                        .diskSnapId()
                                                            .description(disk.description())
                                                            .storageType(disk.storageType())
                                                            .build();
                                                })
                                        .collect(Collectors.toList())
                                )
//                                .nicVoList(
//                                        nicList.stream()
//                                                .map(nic ->
//                                                        NicVo.builder()
//                                                                .build()
//                                                )
//                                                .collect(Collectors.toList())
//                                )
                                .build();
                    }else {  // 스냅샷 기본

                        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();

                        return SnapshotVo.builder()
                                .id(snapshot.id())
                                .description(snapshot.description())
                                .date(sdf.format(snapshot.date().getTime()))  //뭐가 안맞음
                                .status(snapshot.snapshotStatus().value())
                                .persistMemorystate(snapshot.persistMemorystate())
                                .setMemory(vm.memory())
                                .guaranteedMemory(vm.memoryPolicy().guaranteed())
                                .cpuCore(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().threadsAsInteger())
                                .sDiskList(
                                    daList.stream()
                                            .map(diskAttachment -> {
                                                String diskId = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(diskAttachment.id()).get().send().attachment().disk().id();
                                                Disk disk = system.disksService().diskService(diskId).get().send().disk();
                                                return SnapshotDiskVo.builder()
                                                        .status(disk.status())  // ok면 up상태인거 같음
                                                        .alias(disk.name())
                                                        .virtualSize(disk.provisionedSize())
                                                        .actualSize(disk.actualSize())
                                                        .sparse(disk.sparse() ? "sparse" : "?")
                                                        .diskInterface(diskAttachment.interface_())
//                                                        .date()
//                                                        .diskSnapId()
                                                        .description(disk.description())
                                                        .storageType(disk.storageType())
                                                        .build();
                                            }).collect(Collectors.toList())
                                )
                                .build();
                    }
                })
                .collect(Collectors.toList());
    }

    // 애플리케이션
    @Override
    public List<ApplicationVo> getApplication(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Application> appList = system.vmsService().vmService(id).applicationsService().list().send().applications();

        return appList.stream()
                .map(application ->
                    ApplicationVo.builder()
                            .name(application.name())
                            .build()
                )
                .collect(Collectors.toList());
    }

    // TODO 안된거임
    // 선호도 그룹
    @Override
    public List<AffinityGroupVo> getAffinitygroup(String id) {
        SystemService system = admin.getConnection().systemService();
        Vm vm = system.vmsService().vmService(id).get().send().vm();
        List<AffinityGroup> affinityGroupList = system.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().list().send().groups();

        log.info("가상머신 선호도그룹 목록");
        return affinityGroupList.stream()
                .map(ag ->
                        AffinityGroupVo.builder()
                                .id(ag.id())
                                .name(ag.name())
                                .description(ag.description())
                                .status(ag.broken())
                                .priority(ag.priority().intValue())  // 우선순위
                                .positive(ag.positivePresent() && ag.positive())
                                .vmEnabled(ag.vmsRule().enabled())
                                .vmPositive(ag.vmsRule().positive())
                                .vmEnforcing(ag.vmsRule().enforcing())
                                .hostEnabled(ag.hostsRule().enabled())
                                .hostPositive(ag.hostsRule().positive())
                                .hostEnforcing(ag.hostsRule().enforcing())
                                .hostMembers(ag.hostsPresent() ? commonService.getAgHostList(system, id, ag.id()) : null)
                                .vmMembers(ag.vmsPresent() ? commonService.getAgVmList(system, id, ag.id()) : null)
                                .hostLabels(ag.hostLabelsPresent() ? commonService.getLabelName(system, ag.hostLabels().get(0).id()) : null)
                                .vmLabels(ag.vmLabelsPresent() ? commonService.getLabelName(system, ag.vmLabels().get(0).id()) : null)
                                .build())
                .collect(Collectors.toList());
    }

    // 선호도 레이블
    @Override
    public List<AffinityLabelVo> getAffinitylabel(String id) {
        SystemService system = admin.getConnection().systemService();
        List<AffinityLabel> affinityLabelList = system.vmsService().vmService(id).affinityLabelsService().list().follow("hosts,vms").send().label();

        log.info("가상머신 선호도 레이블");
        return affinityLabelList.stream()
                .map(al ->
                        AffinityLabelVo.builder()
                                .id(al.id())
                                .name(al.name())
                                .hosts(commonService.getHostLabelMember(system, al.id()))
                                .vms(commonService.getVmLabelMember(system, al.id()))
                                .build())
                .collect(Collectors.toList());
    }

    // 게스트 정보
    @Override
    public GuestInfoVo getGuestInfo(String id) {
        SystemService system = admin.getConnection().systemService();
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        GuestInfoVo gif = null;
        if(vm.guestOperatingSystemPresent()){
            gif = GuestInfoVo.builder()
                        .architecture(vm.guestOperatingSystem().architecture())
                        .type(vm.guestOperatingSystem().family())
                        .kernalVersion(vm.guestOperatingSystem().kernel().version().fullVersion())
                        .os(vm.guestOperatingSystem().distribution()+" "+vm.guestOperatingSystem().version().major())
                        .guestTime(vm.guestTimeZone().name()+vm.guestTimeZone().utcOffset())
                    .build();
        }
        log.info("가상머신 게스트 정보");
        return gif;
    }

    // 권한
    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Permission> permissionList = system.vmsService().vmService(id).permissionsService().list().send().permissions();
        
        return commonService.getPermission(system, permissionList);
    }


    // 이벤트
    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();
        List<Event> eventList = systemService.eventsService().list().send().events();
        Vm vm = systemService.vmsService().vmService(id).get().send().vm();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        return eventList.stream()
                .filter(event -> event.vmPresent() && event.vm().name().equals(vm.name()))
                .map(event ->
                        EventVo.builder()
                                .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                                .time(sdf.format(event.time()))
                                .message(event.description())
                                .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                                .source(event.origin())
                                .build()
                )
                .collect(Collectors.toList());
    }


}
