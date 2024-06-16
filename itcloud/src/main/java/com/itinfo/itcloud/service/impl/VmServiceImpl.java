package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.OsVo;
import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.*;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkFilterParameterVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItAffinityService;
import com.itinfo.itcloud.service.ItVmService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;


// r저거 고쳐야됨 model
@Service
@Slf4j
public class VmServiceImpl implements ItVmService {
    @Autowired private AdminConnectionService admin;
    @Autowired private CommonService commonService;
    @Autowired private ItAffinityService affinityService;


    /**
     * 가상머신 목록
     * @return 가상머신 목록
     */
    @Override
    public List<VmVo> getList() {
        SystemService system = admin.getConnection().systemService();
        List<Vm> vmList = system.vmsService().list().allContent(true).send().vms();

        log.info("가상머신 리스트");
        return vmList.stream()
                .map(vm -> {
                    Cluster cluster = system.clustersService().clusterService(vm.cluster().id()).get().send().cluster();
                    DataCenter dataCenter = system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter();

                    return VmVo.builder()
                            .status(TypeExtKt.findVmStatus(vm.status()))
                            .hostEngineVm(vm.origin().equals("managed_hosted_engine"))  // 엔진여부
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




    // region: 가상머신 생성 위한 목록

    /**
     * 가상머신 생성 - 클러스터 리스트
     * @return 클러스터 리스트
     */
    @Override
    public List<ClusterVo> setClusterList() {
        SystemService system = admin.getConnection().systemService();
        return system.clustersService().list().send().clusters().stream()
                .filter(cluster -> cluster.dataCenterPresent() && cluster.cpuPresent())
                .map(cluster ->
                        ClusterVo.builder()
                            .id(cluster.id())
                            .name(cluster.name())
                            .datacenterId(cluster.dataCenter().id())
                            .datacenterName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 - 템플릿 목록
     * @return 템플릿 목록
     */
    @Override
    public List<TemplateVo> setTemplateList() {
        SystemService system = admin.getConnection().systemService();
        return system.templatesService().list().send().templates().stream()
                .map(template ->
                        TemplateVo.builder()
                                .id(template.id())
                                .name(template.name())
                                .versionName(template.versionPresent() ? template.version().versionName() : "")
                                .versionNum(template.versionPresent() ? template.version().versionNumberAsInteger() : 0)
                                .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 - 인스턴스 이미지 - 연결
     * @return 가상 디스크 - 이미지 목록
     */
    @Override
    public List<DiskVo> setDiskList() {
        SystemService system = admin.getConnection().systemService();
        List<Disk> diskList = system.disksService().list().send().disks();

        List<String> attDiskIdList = system.vmsService().list().send().vms().stream()
                .flatMap(vm -> system.vmsService().vmService(vm.id()).diskAttachmentsService().list().send().attachments().stream())
                .map(DiskAttachment::id)
                .collect(Collectors.toList());

        return diskList.stream()
                .filter(disk ->
                        (disk.storageType() == DiskStorageType.IMAGE || disk.storageType() == DiskStorageType.LUN)
                        && disk.contentType() == DiskContentType.DATA
                        && !attDiskIdList.contains(disk.id())
                )
                .map(disk -> {
                    StorageDomain storageDomain = system.storageDomainsService().storageDomainService(disk.storageDomains().get(0).id()).get().send().storageDomain();

                    return DiskVo.builder()
                            .id(disk.id())
                            .alias(disk.alias())
                            .description(disk.description())
                            .virtualSize(disk.provisionedSize())
                            .actualSize(disk.actualSize())
                            .shareable(disk.shareable())
                            .domainVo(
                                    DomainVo.builder()
                                            .id(storageDomain.id())
                                            .name(storageDomain.name())
                                            .build()
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 - 인스턴스 이미지 - 생성
     * @param clusterId 클러스터 id
     * @return 스토리지 도메인 목록
     */
    @Override
    public List<DomainVo> setDiskAttach(String clusterId) {
        SystemService system = admin.getConnection().systemService();
        Cluster cluster = system.clustersService().clusterService(clusterId).get().send().cluster();
        List<StorageDomain> sdList = system.dataCentersService().dataCenterService(cluster.dataCenter().id()).storageDomainsService().list().send().storageDomains();

        return sdList.stream()
                .map(storageDomain -> {
                    List<DiskProfile> dpList = system.storageDomainsService().storageDomainService(storageDomain.id()).diskProfilesService().list().send().profiles();

                    return DomainVo.builder()
                            .id(storageDomain.id())
                            .name(storageDomain.name())
                            .diskSize(storageDomain.available().add(storageDomain.used()))
                            .availableSize(storageDomain.available())
                            .profileVoList(
                                    dpList.stream()
                                            .map(diskProfile ->
                                                    DiskProfileVo.builder()
                                                            .id(diskProfile.id())
                                                            .name(diskProfile.name())
                                                            .build()
                                            )
                                            .collect(Collectors.toList())
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 -  vNic 목록 출력 (가상머신 생성, 네트워크 인터페이스 생성)
     * @param clusterId 클러스터 id
     * @return vNic 목록
     */
    @Override
    public List<VnicProfileVo> setVnic(String clusterId) {
        // 데이터 센터가 같아야함
        SystemService system = admin.getConnection().systemService();
        List<VnicProfile> vnicProfileList = system.vnicProfilesService().list().send().profiles();
        String dcId = system.clustersService().clusterService(clusterId).get().send().cluster().dataCenter().id();

        return vnicProfileList.stream()
                .filter(vNic -> {
                    Network network = system.networksService().networkService(vNic.network().id()).get().send().network();
                    return network.dataCenter().id().equals(dcId);
                })
                .map(vNic -> {
                    Network network = system.networksService().networkService(vNic.network().id()).get().send().network();
                    return VnicProfileVo.builder()
                            .id(vNic.id())
                            .name(vNic.name())
                            .networkName(network.name())
                            .provider(network.externalProviderPresent())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 - 호스트 - 호스트 리스트
     * @param clusterId 클러스터 id
     * @return 호스트 리스트
     */
    @Override
    public List<IdentifiedVo> setHostList(String clusterId) {
        SystemService system = admin.getConnection().systemService();
        return system.hostsService().list().send().hosts().stream()
                .filter(host -> host.cluster().id().equals(clusterId))
                .map(host ->
                        IdentifiedVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 - 고가용성 - 임대 대상 스토리지 도메인 목록
     * @return 스토리지 도메인 목록
     */
    @Override
    public List<IdentifiedVo> setStorageList() {
        SystemService system = admin.getConnection().systemService();
        return system.storageDomainsService().list().send().storageDomains().stream()
                .filter(storageDomain -> !storageDomain.statusPresent())
                .map(storageDomain ->
                    IdentifiedVo.builder()
                            .id(storageDomain.id())
                            .name(storageDomain.name())
                            .build()
                )
                .collect(Collectors.toList());
    }

    /**
      * 가상머신 생성 - 리소스할당 - cpuProfile 목록 출력
      * @param clusterId 클러스터 id
      * @return cpuProfile 목록
      */
    @Override
    public List<IdentifiedVo> setCpuProfileList(String clusterId) {
        SystemService system = admin.getConnection().systemService();
        return system.cpuProfilesService().list().send().profile().stream()
                .filter(cpuProfile -> cpuProfile.cluster().id().equals(clusterId))
                .map(cpuProfile ->
                        IdentifiedVo.builder()
                                .id(cpuProfile.id())
                                .name(cpuProfile.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 - 부트 옵션 - 생성 시 필요한 CD/DVD 연결할 ISO 목록
     * @return ISO 목록
     */
    @Override
    public List<IdentifiedVo> setIsoImage(){
        SystemService system = admin.getConnection().systemService();
        List<Disk> diskList = system.disksService().list().send().disks();

        // 이미 쓰고있는지 확인?해야하나?
        return system.disksService().list().send().disks().stream()
                .filter(disk -> disk.contentType().equals(DiskContentType.ISO))
                .map(disk ->
                        IdentifiedVo.builder()
                                .id(disk.id())
                                .name(disk.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 - 선호도 - 선호도 그룹 목록
     * @param clusterId 클러스터 id
     * @return 선호도 그룹 목록
     */
    @Override
    public List<IdentifiedVo> setAgList(String clusterId) {
        SystemService system = admin.getConnection().systemService();
        return system.clustersService().clusterService(clusterId).affinityGroupsService().list().send().groups().stream()
                .map(affinityGroup ->
                        IdentifiedVo.builder()
                                .id(affinityGroup.id())
                                .name(affinityGroup.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 가상머신 생성 - 선호도 - 선호도 레이블 목록
     * @return 선호도 레이블 목록
     */
    @Override
    public List<IdentifiedVo> setAlList() {
        SystemService system = admin.getConnection().systemService();
        return system.affinityLabelsService().list().send().labels().stream()
                .map(affinityLabel ->
                        IdentifiedVo.builder()
                                .id(affinityLabel.id())
                                .name(affinityLabel.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    // endregion


    /**
     * 가상머신 생성
     * @param vmVo 가상머신 객체
     * @return vm 201(create) 404(fail)
     */
    @Override
    public CommonVo<Boolean> addVm(VmCreateVo vmVo) {
        SystemService system = admin.getConnection().systemService();
        VmsService vmsService = system.vmsService();

        if (!vmsService.list().search("name=" + vmVo.getName()).send().vms().isEmpty()) {
            log.error("가상머신 이름 중복");
            return CommonVo.duplicateResponse();
        }

        VmBuilder vmBuilder = new VmBuilder();
        getVmInfo(vmBuilder, vmVo); // 일반
        getVmSystem(system, vmBuilder, vmVo);   // 시스템
        getVmInit(vmBuilder, vmVo); // 초기 실행
        getVmHost(vmBuilder, vmVo); // 호스트
        getVmResource(vmBuilder, vmVo); // 리소스 할당
        getVmHa(vmBuilder, vmVo);   // 고가용성
        getVmBoot(vmBuilder, vmVo); // 부트옵션

        Vm vm = vmsService.add().vm(vmBuilder.build()).send().vm();
        VmService vmService = vmsService.vmService(vm.id());

        // 가상머신 만들고 nic 붙이고 disk 붙이는 식
        try{
            if (vmVo.getVnicList() != null) {
                addVmNic(system, vm, vmVo);
            }

            // disk가 있다면
            if (vmVo.getVDiskList() != null) {
                addVmDisk(system, vm, vmVo.getVDiskList());
            }

            // 이것도 vm id가 있어야 생성가능
            if (vmVo.getVmBootVo().getConnId() != null) {
                vmService.cdromsService().add().cdrom(new CdromBuilder().file(new FileBuilder().id(vmVo.getVmBootVo().getConnId())).build()).send();
            }

            if(expectStatus(vmService, VmStatus.DOWN, 3000, 900000)){
                log.info("가상머신 생성 완료: " + vm.name());
                return CommonVo.createResponse();
            } else {
                log.error("가상머신 생성 시간 초과: {}", vm.name());
                return CommonVo.failResponse("가상머신 생성 시간 초과");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("가상머신 생성 실패");
            return CommonVo.failResponse("가상머신 생성 실패");
        }
    }



    // region: 가상머신 생성

    /**
     * 가상머신 생성 - 일반 + 시스템-일반 시간
     * @param vmBuilder 가상머신 빌더
     * @param vmVo 가상머신 객체
     */
    private void getVmInfo(VmBuilder vmBuilder, VmCreateVo vmVo) {
        vmBuilder.cluster(new ClusterBuilder().id(vmVo.getClusterId())) //  클러스터
                .template(new TemplateBuilder().id(vmVo.getTemplateId()))   // 템플릿
                .bios(new BiosBuilder().type(BiosType.valueOf(vmVo.getChipsetType())))  // 칩셋/펌웨어
                .type(VmType.valueOf(vmVo.getOption()))   // 최적화 옵션
                .name(vmVo.getName())
                .description(vmVo.getDescription())
                .comment(vmVo.getComment())
                .stateless(vmVo.isStateless())      // 상태 비저장
                .startPaused(vmVo.isStartPaused())  // 일시정지
                .deleteProtected(vmVo.isDeleteProtected()); // 삭제 방지
                // 보관?
    }


    /**
     * 가상머신 생성 - 인스턴스 이미지 [ 연결 / 생성 ]
     * 디스크 여러개 가능 (연결+생성, 연결+연결, 생성+생성)
     * @param system
     * @param vm
     * @param vDiskVoList
     * @return
     */
    private CommonVo<Boolean> addVmDisk(SystemService system, Vm vm, List<VDiskVo> vDiskVoList) throws Exception{
        try {
            DiskAttachmentsService dasService = system.vmsService().vmService(vm.id()).diskAttachmentsService();
            DiskAttachmentBuilder daBuilder;
            boolean bootableDiskExists = dasService.list().send().attachments().stream().anyMatch(DiskAttachment::bootable);

            for (VDiskVo vDiskVo : vDiskVoList) {
                log.debug("null: {}", vDiskVo.getVDiskImageVo().getDiskId() != null);

                if(vDiskVo.getVDiskImageVo().getDiskId() != null){
                    // 디스크 이미지 연결
                    Disk disk = system.disksService().diskService(vDiskVo.getVDiskImageVo().getDiskId()).get().send().disk();

                    daBuilder = attachDisk(disk, vDiskVo, vDiskVo.getVDiskImageVo().isBootable(), true);
                    log.info("디스크 연결");
                }else {
                    // 디스크 이미지 생성
                    DiskBuilder diskBuilder = createDisk(vDiskVo);
                    Disk disk = system.disksService().add().disk(diskBuilder).send().disk();
                    DiskService diskService = system.disksService().diskService(disk.id());

                    // 디스크 상태 확인 (LOCK -> OK)
                    if (expectStatus(diskService, DiskStatus.OK, 1000, 60000)) {
                        log.info("디스크 생성 완료: {}", disk.name());
                    } else {
                        log.error("디스크 생성 시간 초과: {}", disk.name());
                        return CommonVo.failResponse("생성 시간 초과");
                    }

                    boolean isBootable = bootableFlag(bootableDiskExists, vDiskVo);
                    bootableDiskExists |= isBootable;

                    daBuilder = attachDisk(disk, vDiskVo, isBootable, false);
                }
                // 추가된 디스크를 VM에 붙임
                dasService.add().attachment(daBuilder).send().attachment();
            }
            log.info("디스크 붙이기 완료");
            return CommonVo.createResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonVo.failResponse(vm.name() + " 디스크 생성 실패");
        }
    }


    /**
     * Disk 생성 정보
     * @param vDiskVo
     * @return
     */
    private DiskBuilder createDisk(VDiskVo vDiskVo){
        return new DiskBuilder()
                .provisionedSize(BigInteger.valueOf(vDiskVo.getVDiskImageVo().getSize()).multiply(BigInteger.valueOf(1024).pow(3))) // 값 받은 것을 byte로 변환하여 준다
                .alias(vDiskVo.getVDiskImageVo().getAlias())
                .description(vDiskVo.getVDiskImageVo().getDescription())
                .storageDomains(new StorageDomain[]{new StorageDomainBuilder().id(vDiskVo.getVDiskImageVo().getStorageDomainId()).build()})
                .sparse(vDiskVo.getVDiskImageVo().isAllocationPolicy()) // 할당정책: 씬 true
                .diskProfile(new DiskProfileBuilder().id(vDiskVo.getVDiskImageVo().getDiskProfile()).build()) // 없어도 상관없음
                .wipeAfterDelete(vDiskVo.getVDiskImageVo().isWipeAfterDelete()) // 삭제후 초기화
                .shareable(vDiskVo.getVDiskImageVo().isShareable()) // 공유 가능 (공유가능 o 이라면 증분백업 안됨 FRONT에서 막기?)
                .backup(vDiskVo.getVDiskImageVo().isBackup() ? DiskBackup.INCREMENTAL : DiskBackup.NONE) // 증분 백업 사용(기본이 true)
                .format(vDiskVo.getVDiskImageVo().isBackup() ? DiskFormat.COW : DiskFormat.RAW); // 백업 안하면 RAW
    }


    /**
     * vm에 디스크 붙이기
     * @param disk
     * @param vDiskVo
     * @param isBootable
     * @param conn conn이 true면 연결, false면 생성
     * @return
     */
    private DiskAttachmentBuilder attachDisk(Disk disk, VDiskVo vDiskVo, boolean isBootable, boolean conn){
        return new DiskAttachmentBuilder()
                .disk(conn ? new DiskBuilder().id(disk.id()).build() : disk)
                .active(true)
                .interface_(vDiskVo.getVDiskImageVo().getInterfaces())
                .bootable(conn ? vDiskVo.getVDiskImageVo().isBootable() : isBootable)
                .readOnly(vDiskVo.getVDiskImageVo().isReadOnly());
    }


    /**
     * 부팅가능한 디스크는 한개만 설정가능
     * @param bootableDiskExists
     * @param vDiskVo
     * @return
     */
    private boolean bootableFlag(boolean bootableDiskExists, VDiskVo vDiskVo){
        boolean isBootable = vDiskVo.getVDiskImageVo().isBootable();
        if (bootableDiskExists && isBootable) {
            log.warn("이미 부팅 가능한 디스크가 존재하므로 디스크는 부팅 불가능으로 설정됨");
            isBootable = false;
        } 
        return isBootable;
    }


    /**
     * 가상머신 생성 - 디스크 생성 상태확인
     * @param diskService
     * @param expectStatus
     * @param interval
     * @param timeout
     * @return
     * @throws InterruptedException
     */
    private boolean expectStatus(DiskService diskService, DiskStatus expectStatus, long interval, long timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (true) {
            Disk currentDisk = diskService.get().send().disk();
            DiskStatus status = currentDisk.status();

            if (status == expectStatus) {
                return true;
            } else if (System.currentTimeMillis() - startTime > timeout) {
                return false;
            }

            log.info("디스크 상태: {}", status);
            Thread.sleep(interval);
        }
    }


    /**
     * vm 생성 - vnic
     * @param system
     * @param vm
     * @param vmVo
     * @return
     */
    private CommonVo<Boolean> addVmNic(SystemService system, Vm vm, VmCreateVo vmVo) {
        // 연결할때 문제생길거같음
        try {
            VmNicsService vmNicsService = system.vmsService().vmService(vm.id()).nicsService();
            List<NicBuilder> nicBuilders =
                    vmVo.getVnicList().stream()
                        .map(identifiedVo -> new NicBuilder()
                                .name("nic" + (vmVo.getVnicList().indexOf(identifiedVo) + 1))
                                .vnicProfile(new VnicProfileBuilder().id(identifiedVo.getId()).build()))
                        .collect(Collectors.toList());

            for (NicBuilder nicBuilder : nicBuilders) {
                vmNicsService.add().nic(nicBuilder).send();
            }

            log.info(vm.name() + " vnic 생성 성공");
            return CommonVo.createResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("vnic 생성 실패, {}", e.getMessage());
            return CommonVo.failResponse(vm.name() + " vnic 생성 실패");
        }
    }



    /**
     * 가상머신 - 시스템
     * @param vmBuilder 가상머신 빌더
     * @param vmVo 가상머신 객체
     */
    private void getVmSystem(SystemService system, VmBuilder vmBuilder, VmCreateVo vmVo){
        BigInteger convertMb = BigInteger.valueOf(1024).pow(2);

        vmBuilder.timeZone(new TimeZoneBuilder().name(vmVo.getVmSystemVo().getTimeOffset())); // 시스템-일반 하드웨어 클럭의 시간 오프셋

        // 인스턴스 타입이 지정되어 있다면
        if (!"".equals(vmVo.getVmSystemVo().getInstanceType())) {
            InstanceType it = system.instanceTypesService().list().search("name=" + vmVo.getVmSystemVo().getInstanceType()).send().instanceType().get(0);
            vmBuilder.instanceType(it);
        } else {    // 사용자 정의 값
            vmBuilder
                    .memory(BigInteger.valueOf(vmVo.getVmSystemVo().getMemorySize()).multiply(convertMb))
                    .memoryPolicy(new MemoryPolicyBuilder()
                            .max(BigInteger.valueOf(vmVo.getVmSystemVo().getMemoryMax()).multiply(convertMb))
                            .ballooning(vmVo.getVmResourceVo().isMemoryBalloon())   // 리소스할당- 메모리 balloon 활성화
                            .guaranteed(BigInteger.valueOf(vmVo.getVmSystemVo().getMemoryActual()).multiply(convertMb)))
                    .cpu(new CpuBuilder().topology(new CpuTopologyBuilder()
                                                    .cores(vmVo.getVmSystemVo().getVCpuSocketCore())
                                                    .sockets(vmVo.getVmSystemVo().getVCpuSocket())
                                                    .threads(vmVo.getVmSystemVo().getVCpuCoreThread())));
        }
    }

    /**
     * 가상머신 - 초기 실행
     * @param vmBuilder 가상머신 빌더
     * @param vmVo 가상머신 객체
     */
    private void getVmInit(VmBuilder vmBuilder, VmCreateVo vmVo){
        if (vmVo.getVmInitVo().isCloudInit()) {
            vmBuilder.initialization(new InitializationBuilder()
                    .hostName(vmVo.getVmInitVo().getHostName())
                    .timezone(vmVo.getVmInitVo().getTimeStandard())  // Asia/Seoul
                    .customScript(vmVo.getVmInitVo().getScript()));
        }
    }


    /**
     * 가상머신 - 호스트
     * @param vmBuilder 가상머신 빌더
     * @param vmVo 가상머신 객체
     */
    private void getVmHost(VmBuilder vmBuilder, VmCreateVo vmVo){
        VmPlacementPolicyBuilder placementBuilder = new VmPlacementPolicyBuilder();

        // 실행 호스트
        if (!vmVo.getVmHostVo().isClusterHost()) {  // 특정 호스트를 선택한다면 무조건 한개는 존재
            // 선택된 호스트 전부 넣기
            placementBuilder.hosts(
                    vmVo.getVmHostVo().getSelectHostId().stream()
                            .map(hostId -> new HostBuilder().id(hostId).build())
                            .collect(Collectors.toList())
            );
        }

        // 마이그레이션: 사용자 정의 일때만 마이그레이션 모드 설정가능
        vmBuilder.placementPolicy(placementBuilder.affinity(
                                    "none".equals(vmVo.getVmSystemVo().getInstanceType()) ?
                                        VmAffinity.valueOf(vmVo.getVmHostVo().getMigrationMode()) : VmAffinity.MIGRATABLE))
                .migration(
                        // 정책은 찾을 수가 없음, parallel Migrations 안보임, 암호화
                        new MigrationOptionsBuilder().encrypted(vmVo.getVmHostVo().getMigrationEncrypt()).build());
//                        .numaNodes(new NumaNodeBuilder().build()) // numa
    }

    /**
     * 가상머신 - 고가용성
     * @param vmBuilder 가상머신 빌더
     * @param vmVo 가상머신 객체
     */
    private void getVmHa(VmBuilder vmBuilder, VmCreateVo vmVo){
        // isHa: 고가용성 여부
        // 재개동작 모르겠음
        vmBuilder.highAvailability( new HighAvailabilityBuilder().enabled(vmVo.getVmHaVo().isHa()).priority(vmVo.getVmHaVo().getPriority()));

        if (vmVo.getVmHaVo().isHa()) {       // 스토리지 도메인 지정
            // 선택안햇을경우
            vmBuilder.lease(new StorageDomainLeaseBuilder().storageDomain(new StorageDomainBuilder().id(vmVo.getVmHaVo().getVmStorageDomainId())));
        }
    }


    /**
     * 가상머신 - 리소스 할당
     * @param vmBuilder 가상머신 빌더
     * @param vmVo 가상머신 객체
     */
    private void getVmResource(VmBuilder vmBuilder, VmCreateVo vmVo){
        // CPU 할당
        vmBuilder
                .cpuProfile(new CpuProfileBuilder().id(vmVo.getVmResourceVo().getCpuProfileId()))
                .cpuShares(vmVo.getVmResourceVo().getCpuShare())
                .autoPinningPolicy("RESIZE_AND_PIN_NUMA".equals(vmVo.getVmResourceVo().getCpuPinningPolicy())
                        ? AutoPinningPolicy.ADJUST : AutoPinningPolicy.DISABLED)
                .cpuPinningPolicy(CpuPinningPolicy.valueOf(vmVo.getVmResourceVo().getCpuPinningPolicy()))
                .virtioScsiMultiQueuesEnabled(vmVo.getVmResourceVo().isMultiQue()); // VirtIO-SCSI 활성화
    }


    /**
     * 가상머신 - 부트 옵션
     * @param vmBuilder 가상머신 빌더
     * @param vmVo 가상머신 객체
     */
    private void getVmBoot(VmBuilder vmBuilder, VmCreateVo vmVo){
        vmBuilder.os(new OperatingSystemBuilder()
                .type(vmVo.getOs()) // 일반 - 운영시스템
                .boot(new BootBuilder().devices(
                        vmVo.getVmBootVo().getDeviceList().stream()
                                .map(BootDevice::valueOf)
                                .collect(Collectors.toList()))));
//                .bios(new BiosBuilder().type(BiosType.valueOf(vmVo.getChipsetType())))  // 칩셋/펌웨어
//                        .bootMenu(new BootMenuBuilder().enabled(vmVo.getVmBootVo().isBootingMenu()))
    }


    // endregion






    /**
     * 가상머신 편집 창
     * @param id 가상머신 id
     * @return 가상머신 정보
     */
    @Override
    public VmCreateVo setVm(String id) {
        SystemService system = admin.getConnection().systemService();
        Vm vm = system.vmsService().vmService(id).get().send().vm();
        Cluster cluster = system.clustersService().clusterService(vm.cluster().id()).get().send().cluster();
        List<DiskAttachment> daList = system.vmsService().vmService(id).diskAttachmentsService().list().send().attachments();
        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();

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
                                .map(da -> {
                                    Disk disk = system.disksService().diskService(da.disk().id()).get().send().disk();
                                    return VDiskVo.builder()
                                            .vDiskImageVo(
                                                    VDiskImageVo.builder()
                                                            .diskId(disk.id())
                                                            .alias(disk.alias())
                                                            .size(disk.provisionedSize().divide(convertMb).longValue())  //
                                                            .bootable(da.bootable())
                                                            .build()
                                            )
                                            .build();
                                })
                                .collect(Collectors.toList())
                )
                .vnicList(
                        nicList.stream()
                                .map(nic -> {
                                    VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
                                    Network network = system.networksService().networkService(vnicProfile.network().id()).get().send().network();

                                    return VnicProfileVo.builder()
                                            .nicName(nic.name())
                                            .id(vnicProfile.id())
                                            .name(vnicProfile.name())
                                            .networkName(network.name())
                                            .build();
                                })
                                .collect(Collectors.toList())
                )
                .vmSystemVo(
                        VmSystemVo.builder()
                                .memorySize(vm.memory().divide(convertMb).longValue())
                                .memoryActual(vm.memoryPolicy().guaranteed().divide(convertMb).longValue())
                                .memoryMax(vm.memoryPolicy().max().divide(convertMb).longValue())
                                .vCpuCnt(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().threadsAsInteger())
                                .vCpuSocket(vm.cpu().topology().socketsAsInteger())
                                .vCpuSocketCore(vm.cpu().topology().coresAsInteger())
                                .vCpuCoreThread(vm.cpu().topology().threadsAsInteger())
                                .timeOffset(vm.timeZone().name())
                                .build()
                )
                .vmInitVo(
                        VmInitVo.builder()
                                .cloudInit(vm.initializationPresent())
                                .hostName(vm.initializationPresent() ? vm.initialization().hostName() : "")
                                .build()
                )
//                .vmHostVo(
//                        VmHostVo.builder()
//                                .clusterHost(vm.placementPolicy().affinity().)
//                                .build()
//                )
//                .vmHaVo()
//                .vmResourceVo()
//                .vmBootVo()
//                .agVoList()
//                .alVoList()
                .build();
    }


    // 가상머신 편집
    @Override
    public CommonVo<Boolean> editVm(String id, VmCreateVo vmCreateVo) {
        SystemService system = admin.getConnection().systemService();
        VmService vmService = system.vmsService().vmService(id);



        return null;
    }

    // 가상머신 삭제
    @Override
    public CommonVo<Boolean> deleteVm(String id, boolean disk) {
        SystemService system = admin.getConnection().systemService();
        VmService vmService = system.vmsService().vmService(id);

        try {
            if (!vmService.get().send().vm().deleteProtected()) {  // 가상머신 삭제방지 여부
                // detachOnly => true 면 가상머신만 삭제/ false면 디스크도 삭제
                // 근데 disk의 값은 true면 디스크 삭제
                vmService.remove().detachOnly(!disk).send();

                log.info(disk ? "가상머신/디스크 삭제 성공" : "가상머신 삭제 성공");
                return CommonVo.successResponse();
            } else {
                log.error("삭제방지 모드를 해제하세요");
                return CommonVo.failResponse("삭제방지 모드를 해제");
            }
        } catch (Exception e) {
            log.error("가상머신 삭제 실패 {}", e.getMessage());
            return CommonVo.failResponse("가상머신 삭제 실패");
        }
    }

    @Override
    public VmStatus getStatus(String id) {
        SystemService system = admin.getConnection().systemService();
        return system.vmsService().vmService(id).get().send().vm().status();
    }



    // region: Action

    // 가상머신 실행
    @Override
    public CommonVo<Boolean> startVm(String id) {
        SystemService system = admin.getConnection().systemService();
        VmService vmService = system.vmsService().vmService(id);
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        try {
            vmService.start().useCloudInit(vm.initializationPresent()).send();

            log.info(vm.initializationPresent() ? "가상머신 cloudinit 시작" : "가상머신 시작");
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("가상머신 시작 실패 : {}", e.getMessage());
            return CommonVo.failResponse("");
        }
    }

    // 가상머신 일시정지
    @Override
    public CommonVo<Boolean> pauseVm(String id) {
        SystemService system = admin.getConnection().systemService();

        try {
            system.vmsService().vmService(id).suspend().send();

            log.info("가상머신 일시정지");
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonVo.failResponse("");
        }
    }

    // 가상머신 전원끔
    @Override
    public CommonVo<Boolean> stopVm(String id) {
        SystemService system = admin.getConnection().systemService();

        try {
            system.vmsService().vmService(id).stop().send();
            
            log.info("가상머신 전원끄기");
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonVo.failResponse("");
        }
    }


    // 가상머신 종료
    @Override
    public CommonVo<Boolean> shutdownVm(String id) {
        SystemService system = admin.getConnection().systemService();

        try {
            system.vmsService().vmService(id).shutdown().send();
            
            log.info("가상머신 종료");
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonVo.failResponse("");
        }
    }


    // 가상머신 재부팅
    @Override
    public CommonVo<Boolean> rebootVm(String id) {
        SystemService system = admin.getConnection().systemService();

        try {
            system.vmsService().vmService(id).reboot().send();

            log.info("가상머신 재부팅");
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonVo.failResponse("");
        }
    }

    // 가상머신 재설정
    @Override
    public CommonVo<Boolean> resetVm(String id) {
        SystemService system = admin.getConnection().systemService();

        try {
            system.vmsService().vmService(id).reset().send();

            log.info("가상머신 재설정");
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonVo.failResponse("");
        }
    }

    // 가상머신 마이그레이션
    @Override
    public CommonVo<Boolean> migrateVm(String id, String hostId) {
        SystemService system = admin.getConnection().systemService();

        try {
            system.vmsService().vmService(id)
                    .migrate().host(new HostBuilder().id(hostId)) // TODO
                    .send();

            log.info("가상머신 마이그레이션");
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonVo.failResponse("");
        }
    }

    // 가상머신 마이그레이션 취소
    @Override
    public CommonVo<Boolean> migrateCancelVm(String id) {
        SystemService system = admin.getConnection().systemService();

        try {
            system.vmsService().vmService(id).cancelMigration().send();

            log.info("가상머신 마이그레이션 취소");
            return CommonVo.successResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonVo.failResponse("");
        }
    }


    // endregion



    @Override
    public List<SnapshotDiskVo> setSnapshot(String id) {
        SystemService system = admin.getConnection().systemService();
        List<DiskAttachment> diskAttachmentList = system.vmsService().vmService(id).diskAttachmentsService().list().send().attachments();

        log.info("스냅샷 디스크 목록");
        return diskAttachmentList.stream()
                .map(diskAttachment -> {
                    String diskId = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(diskAttachment.id()).get().send().attachment().disk().id();
                    Disk disk = system.disksService().diskService(diskId).get().send().disk();
                    return SnapshotDiskVo.builder()
                            .daId(diskAttachment.id())
                            .alias(disk.alias())
                            .description(disk.description())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 스냅샵 생성
    @Override
    public CommonVo<Boolean> addSnapshot(SnapshotVo snapshotVo) {
        SystemService system = admin.getConnection().systemService();
//        List<DiskAttachment> diskAttachmentList = system.vmsService().vmService(snapshotVo.getVmId()).diskAttachmentsService().list().send().attachments();
//        SnapshotsService snapshotsService = system.vmsService().vmService(snapshotVo.getVmId()).snapshotsService();

        try {
            // 디스크 선택된것만 들어가야됨
            List<String> s = snapshotVo.getSDiskList().stream()
                    .map(snapshotDiskVo -> snapshotDiskVo.getDaId())
                    .collect(Collectors.toList());

            s.forEach(System.out::println);

//            List<DiskAttachment> da =
//                    diskAttachmentList.stream()
//                                    .map(diskAttachment -> {
//                                        String diskId = system.vmsService().vmService(snapshotVo.getVmId()).diskAttachmentsService().attachmentService(diskAttachment.id()).get().send().attachment().disk().id();
//                                        Disk disk = system.disksService().diskService(diskId).get().send().disk();
//
//
//                                    })
//                                    .collect(Collectors.toList());
//
//            snapshotsService.add().snapshot(new SnapshotBuilder().description(snapshotVo.getDescription()).diskAttachments().build()).send();

            log.info("스냅샷 생성 성공");
            return CommonVo.createResponse();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("스냅샷 생성 실패");
            return CommonVo.failResponse(e.getMessage());
        }

    }


    // 일반
    @Override
    public VmVo getInfo(String id) {
        SystemService system = admin.getConnection().systemService();
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        DecimalFormat df = new DecimalFormat("###,###");

        String hostName = null;
        if (vm.hostPresent()) {
            hostName = system.hostsService().hostService(vm.host().id()).get().send().host().name();
        } else if (!vm.hostPresent() && vm.placementPolicy().hostsPresent()) {
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
                .osSystem(TypeExtKt.findOs(OsVo.valueOf(vm.os().type())))
                .chipsetFirmwareType(TypeExtKt.findBios(vm.bios().type()))
                .priority(TypeExtKt.findPriority(vm.highAvailability().priorityAsInteger()))  // 서버,클라이언트 처리?
                .optimizeOption(TypeExtKt.findVmType(vm.type()))
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

    // 네트워크 인터페이스
    @Override
    public List<NicVo> getNic(String id) {
        SystemService system = admin.getConnection().systemService();
        VmNicsService vmNicsService = system.vmsService().vmService(id).nicsService();
        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();

        return nicList.stream()
                .map(nic -> {
                    List<Statistic> statisticList = vmNicsService.nicService(nic.id()).statisticsService().list().send().statistics();
                    VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
                    String guestInterface = !vmNicsService.nicService(nic.id()).reportedDevicesService().list().send().reportedDevice().isEmpty()
                            ? vmNicsService.nicService(nic.id()).reportedDevicesService().list().send().reportedDevice().get(0).name() : "해당없음";

                    return NicVo.builder()
                            .id(nic.id())
                            .plugged(nic.plugged())  // 연결상태 (연결됨 t, 분리 f)
                            .networkName(system.networksService().networkService(vnicProfile.network().id()).get().send().network().name())
                            .vnicProfileVo(
                                    VnicProfileVo.builder()
                                            .name(vnicProfile.name())       // 프로파일 이름
                                            .portMirroring(vnicProfile.portMirroring()) // 포트미러링
                                            .build()
                            )
                            .name(nic.name())
                            .synced(nic.synced())
                            .linkStatus(nic.linked()) // 링크상태 t/f(정지)
                            .interfaces(nic.interface_().value())
                            .macAddress(nic.macPresent() ? nic.mac().address() : null)
                            .ipv4(commonService.getVmIp(system, id, "v4"))
                            .ipv6(commonService.getVmIp(system, id, "v6"))
//                            .speed()
                            .rxSpeed(commonService.getSpeed(statisticList, "data.current.rx.bps"))
                            .txSpeed(commonService.getSpeed(statisticList, "data.current.tx.bps"))
                            .rxTotalSpeed(commonService.getSpeed(statisticList, "data.total.rx"))
                            .txTotalSpeed(commonService.getSpeed(statisticList, "data.total.tx"))
                            .stop(commonService.getSpeed(statisticList, "errors.total.rx"))
                            .guestInterface(guestInterface)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    // 가상머신 - 새 네트워크 인터페이스
    // 생성창은 필요없음, 왜냐면 프로파일 리스트만 가지고 오면됨
    // public List<VnicProfileVo> setVnic(String clusterId) {} 사용하면 됨
    @Override
    public CommonVo<Boolean> addNic(String id, NicVo nicVo) {
        SystemService system = admin.getConnection().systemService();
        VmNicsService vmNicsService = system.vmsService().vmService(id).nicsService();
        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();
        boolean duplicateName = nicList.stream().anyMatch(nic -> nic.name().equals(nicVo.getName()));  // nic 이름 중복 검사

        try{
            if(!duplicateName) {
                NicBuilder nicBuilder = new NicBuilder();
                nicBuilder
                        .name(nicVo.getName())
                        .vnicProfile(new VnicProfileBuilder().id(nicVo.getVnicProfileVo().getId()))
                        .interface_(NicInterface.valueOf(nicVo.getInterfaces()))
                        .linked(nicVo.isLinkStatus())
                        .plugged(nicVo.isPlugged());

                if (nicVo.getMacAddress() != null) { // mac 주소 중복
                    if (nicList.stream().anyMatch(nic -> nic.mac().address().equals(nicVo.getMacAddress()))) {
                        log.error("중복되는 mac주소");
                        return CommonVo.failResponse("중복되는 mac주소");
                    } else {
                        nicBuilder.mac(new MacBuilder().address(nicVo.getMacAddress()));
                    }
                }

                Nic nic = vmNicsService.add().nic(nicBuilder).send().nic();

                if (nicVo.getNfVoList() != null) {
                    NicNetworkFilterParametersService nfpsService = system.vmsService().vmService(id).nicsService().nicService(nic.id()).networkFilterParametersService();

                    List<NetworkFilterParameter> npList =
                            nicVo.getNfVoList().stream()
                                    .map(nFVo ->
                                            new NetworkFilterParameterBuilder()
                                                    .name(nFVo.getName())
                                                    .value(nFVo.getValue())
                                                    .nic(nic)
                                                    .build()
                                    )
                                    .collect(Collectors.toList());

                    for (NetworkFilterParameter np : npList) {
                        nfpsService.add().parameter(np).send();
                    }
                    log.info("네트워크 필터 생성 완료");
                }

                log.info("가상머신 nic 생성 성공");
                return CommonVo.createResponse();
            }else{
                log.error("nic 이름 중복");
                return CommonVo.failResponse("가상머신 nic 생성 실패");    
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonVo.failResponse("가상머신 nic 생성 실패");
        }
    }


    @Override
    public NicVo setEditNic(String id, String nicId) {
        SystemService system = admin.getConnection().systemService();
        Nic nic = system.vmsService().vmService(id).nicsService().nicService(nicId).get().send().nic();
        List<NetworkFilterParameter> nfpList = system.vmsService().vmService(id).nicsService().nicService(nicId).networkFilterParametersService().list().send().parameters();

        NicVo.NicVoBuilder nicVo = NicVo.builder()
                .id(nicId)
                .name(nic.name())
                .interfaces(nic.interface_().value()) // 유형
                .macAddress(nic.mac().address())  // mac 주소
                .linkStatus(nic.linked())
                .plugged(nic.plugged())
                .nfVoList(
                        nfpList.stream()
                                .map(nfp ->
                                        NetworkFilterParameterVo.builder()
                                                .id(nfp.id())
                                                .name(nfp.name())
                                                .value(nfp.value())
                                                .build()
                                )
                                .collect(Collectors.toList())
                );
        
        // vnicProfile이 있으면 표시
        if(nic.vnicProfilePresent()) {
            VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
            nicVo.vnicProfileVo(
                    VnicProfileVo.builder()
                            .name(vnicProfile.name())
                            .networkName(system.networksService().networkService(vnicProfile.network().id()).get().send().network().name())
                            .build()
            );
        }
        
        return nicVo.build();
    }


    // 가상머신 nic 수정
    @Override
    public CommonVo<Boolean> editNic(String id, NicVo nicVo) {
        SystemService system = admin.getConnection().systemService();
        VmNicService vmNicService = system.vmsService().vmService(id).nicsService().nicService(nicVo.getId());
        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();

        // 이름 중복
        boolean duplicateName =
                system.vmsService().vmService(id).nicsService().list().send().nics().stream()
                .filter(nic -> !nic.id().equals(id))
                .anyMatch(nic -> nic.name().equals(nicVo.getName()));

        try {
            if(!duplicateName) {

                NicBuilder nicBuilder = new NicBuilder();
                nicBuilder
                        .name(nicVo.getName())
                        .vnicProfile(new VnicProfileBuilder().id(nicVo.getVnicProfileVo().getId()))
//                        .interface_(NicInterface.valueOf(nicVo.getInterfaces()))
                        .linked(nicVo.isLinkStatus())
                        .plugged(nicVo.isPlugged());

                Nic nic = vmNicService.update().nic(nicBuilder).send().nic();

                // TODO:HELP
                // 수정창에 nf List가 뜰 예정(id)
                // 추가되면 추가해야하고 삭제되면 삭제해야함
                if (nicVo.getNfVoList() != null) {
                    NicNetworkFilterParametersService nfpsService = system.vmsService().vmService(id).nicsService().nicService(nic.id()).networkFilterParametersService();

                    List<NetworkFilterParameter> npList =
                            nicVo.getNfVoList().stream()
                                    .map(nFVo ->
                                            new NetworkFilterParameterBuilder()
                                                    .name(nFVo.getName())
                                                    .value(nFVo.getValue())
                                                    .nic(nic)
                                                    .build()
                                    )
                                    .collect(Collectors.toList());

                    for (NetworkFilterParameter np : npList) {
                        nfpsService.add().parameter(np).send();
                    }
                    log.info("네트워크 필터 수정 완료");
                }


                log.info("nic 수정 성공");
                return CommonVo.successResponse();
            }else{
                log.error("nic 이름중복");
                return CommonVo.failResponse("nic 이름 중복");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return CommonVo.failResponse(e.getMessage());
        }

    }

    @Override
    public CommonVo<Boolean> deleteNic(String id, String nicId) {
        SystemService system = admin.getConnection().systemService();
        VmNicService vmNicService = system.vmsService().vmService(id).nicsService().nicService(nicId);
        Nic nic = system.vmsService().vmService(id).nicsService().nicService(nicId).get().send().nic();

        try{
            if(!nic.plugged()) {
                vmNicService.remove().send();

                log.info("nic 삭제 성공");
                return CommonVo.successResponse();
            }else{
                log.error("nic가 plug된 상태");
                return CommonVo.failResponse("nic가 plug된 상태");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return CommonVo.failResponse(e.getMessage());
        }
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
                            .virtualSize(disk.provisionedSize())
                            .connection(system.vmsService().vmService(id).get().send().vm().name()) // 연결대상
                            .interfaceName(diskAttachment.interface_().value())
                            .logicalName(diskAttachment.logicalName())
                            .name(disk.name())
                            .status(disk.status().value())  // 상태
                            .description(disk.description())
                            .type(TypeExtKt.findStorageType(disk.storageType()))  // 유형
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 디스크 생성
    @Override
    public CommonVo<Boolean> addDiskImage(String id, VDiskImageVo image) {
        SystemService system = admin.getConnection().systemService();
        DisksService disksService = system.disksService();
        DiskAttachmentsService dasService = system.vmsService().vmService(id).diskAttachmentsService();

        // 이미 부팅 가능한 디스크가 있는지 확인
        boolean bootableDiskExists = dasService.list().send().attachments().stream().anyMatch(DiskAttachment::bootable);

        try{
//            if(!bootableDiskExists && image.isBootable()) {
                Disk disk = disksService.add()
                        .disk(
                                new DiskBuilder()
                                        .provisionedSize(BigInteger.valueOf(image.getSize()).multiply(BigInteger.valueOf(1024).pow(3))) // 값 받은 것을 byte로 변환하여 준다
                                        .alias(image.getAlias())
                                        .description(image.getDescription())
                                        .storageDomains(new StorageDomain[]{new StorageDomainBuilder().id(image.getStorageDomainId()).build()})
                                        .sparse(image.isAllocationPolicy()) // 할당정책: 씬 true
                                        .diskProfile(new DiskProfileBuilder().id(image.getDiskProfile()).build()) // 없어도 상관없음
                                        .wipeAfterDelete(image.isWipeAfterDelete()) // 삭제후 초기화
                                        .shareable(image.isShareable())     // 공유 가능 (공유가능 o 이라면 증분백업 안됨 FRONT에서 막기?)
                                        .backup(image.isBackup() ? DiskBackup.INCREMENTAL : DiskBackup.NONE)    // 증분 백업 사용(기본이 true)
                                        .format(image.isBackup() ? DiskFormat.COW : DiskFormat.RAW) // 백업 안하면 RAW
                                        .build()
                        )
                        .send().disk();

                // 목표: disk 상태가 LOCK임으로, OK가 될 때까지 상태확인
                int retry = 0;
                Disk dTmp = disksService.diskService(disk.id()).get().send().disk();
                while (dTmp.statusPresent() && dTmp.status() == DiskStatus.LOCKED && retry <= 20) {
                    Thread.sleep(1000L);
                    log.debug("retry: {}, disk: {}, {}", retry, dTmp.name(), dTmp.status());
                    dTmp = disksService.diskService(disk.id()).get().send().disk();
                    retry += 1;
                }

                if (dTmp.status() != DiskStatus.OK) {
                    return CommonVo.failResponse("disk가 잠겨있음");
                }

                // 추가된 디스크를 vm에 붙임
                dasService.add()
                        .attachment(
                                new DiskAttachmentBuilder()
                                        .disk(disk)
                                        .interface_(image.getInterfaces())
                                        .bootable(image.isBootable()/*bootable*/)
                                        .active(image.isActive())
                                        .readOnly(image.isReadOnly())
                        )
                        .send().attachment();

                log.info("성공: 디스크 이미지 {} 생성", image.getAlias());
                return CommonVo.createResponse();
//            }else{
//                log.error("실패: 부팅가능한 디스크가 이미 존재");
//                return CommonVo.failResponse("부팅가능한 디스크가 이미 존재");
//            }
        }catch (Exception e){
            log.error("실패: 새 가상 디스크 (이미지) 생성");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> editDiskImage(String id, VDiskImageVo image) {

        return null;
    }

    @Override
    public CommonVo<Boolean> addDiskLun(String id, VDiskLunVo lun) {
        return null;
    }

    @Override
    public CommonVo<Boolean> editDiskLun(String id, VDiskLunVo lun) {
        return null;
    }

    @Override
    public CommonVo<Boolean> deleteDisk(String id, String daId, boolean type) {
        SystemService system = admin.getConnection().systemService();
        DiskAttachment da = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(daId).get().send().attachment();
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        try{
            // 가상머신이 연결되어잇는지, down 상태인지
            if(vm.status() == VmStatus.DOWN) {
                if(type) {   // 완전삭제
                    DiskService diskService = system.disksService().diskService(da.disk().id());
                    Disk disk = system.disksService().diskService(da.disk().id()).get().send().disk();
                    diskService.remove().send();

                    do {
                        log.info("디스크 완전 삭제");
                    } while (!disk.idPresent());

                    log.info("성공: 디스크 삭제");
                    return CommonVo.successResponse();
                }else {
                    DiskAttachmentService daService = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(daId);
                    daService.remove().send();

                    do {
                        log.info("디스크 삭제");
                    } while (da.disk().id().isEmpty());

                    log.info("디스크 삭제");
                    return CommonVo.successResponse();
                }
            }else{
                log.error("실패: 가상머신이 Down이 아님");
                return CommonVo.failResponse("가상머신이 Down이 아님");
            }
        }catch (Exception e){
            log.error("실패: 새 가상 디스크 (이미지) 수정");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 가상머신 - 디스크 활성화
    @Override
    public CommonVo<Boolean> activeDisk(String id, String daId) {
        SystemService system = admin.getConnection().systemService();
        DiskAttachmentService daService = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(daId);
        DiskAttachment da = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(daId).get().send().attachment();

        try {
            if (!da.active()) {
                daService.update().diskAttachment(new DiskAttachmentBuilder().active(true)).send();

                return CommonVo.successResponse();
            } else {
                // TODO:HELP boolean type으로 활성화/비활성화?
                return CommonVo.failResponse("이미 활성화가 되어있음");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("실패: 디스크 활성화");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> deactivateDisk(String id, String daId) {
        SystemService system = admin.getConnection().systemService();
        DiskAttachmentService daService = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(daId);
        DiskAttachment da = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(daId).get().send().attachment();

        try {
            if (da.active()) {
                daService.update().diskAttachment(new DiskAttachmentBuilder().active(false)).send();

                return CommonVo.successResponse();
            } else {
                // TODO:HELP boolean type으로 활성화/비활성화?
                return CommonVo.failResponse("이미 비활성화가 되어있음");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("실패: 디스크 비활성화");
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 가상머신 - 디스크 이동창
    @Override
    public DiskVo setDiskMove(String id, String daId) {
        SystemService system = admin.getConnection().systemService();
        String dcId = system.clustersService().clusterService(system.vmsService().vmService(id).get().send().vm().cluster().id()).get().send().cluster().dataCenter().id();
        String diskId = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(daId).get().send().attachment().disk().id();
        List<StorageDomain> storageDomainList = system.dataCentersService().dataCenterService(dcId).storageDomainsService().list().send().storageDomains();
        Disk disk = system.disksService().diskService(diskId).get().send().disk();

        return DiskVo.builder()
                .id(disk.id())
                .alias(disk.alias())
                .virtualSize(disk.provisionedSize().divide(BigInteger.valueOf(1024).pow(3)))
                .domainVoList(
                        storageDomainList.stream()
                                .filter(storageDomain -> !storageDomain.id().equals(disk.storageDomains().get(0).id())) // 자신의 도메인이 아닐경우
                                .map(storageDomain -> {
                                    List<DiskProfile> profileList = system.diskProfilesService().list().send().profile();
                                    return DomainVo.builder()
                                            .id(storageDomain.id())
                                            .name(storageDomain.name())
                                            .profileVoList(
                                                    profileList.stream()
                                                            .filter(diskProfile -> diskProfile.storageDomain().id().equals(storageDomain.id()))
                                                            .map(diskProfile ->
                                                                DiskProfileVo.builder()
                                                                        .id(diskProfile.id())
                                                                        .name(diskProfile.name())
                                                                        .build()
                                                            )
                                                            .collect(Collectors.toList())
                                            )
                                            .build();
                                })
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public CommonVo<Boolean> moveDisk(String id, String daId, DiskVo diskVo){
        SystemService system = admin.getConnection().systemService();
        DiskAttachment da = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(daId).get().send().attachment();
        DiskService diskService = system.disksService().diskService(da.disk().id());

        try{
            diskService.move()
                    .storageDomain(new StorageDomainBuilder().id(diskVo.getDomainVo().getId()))
                    .diskProfile(new DiskProfileBuilder().id(diskVo.getProfileVo().getId()))
                .send();

            log.info("디스크 이동");
            return CommonVo.successResponse();
        }catch (Exception e){
            e.printStackTrace();
            log.error("실패: 디스크 이동");
            return CommonVo.failResponse(e.getMessage());
        }
    }




















    // TODO
    // 스냅샷
    @Override
    public List<SnapshotVo> getSnapshot(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Snapshot> snapList = system.vmsService().vmService(id).snapshotsService().list().send().snapshots();
        List<DiskAttachment> daList = system.vmsService().vmService(id).diskAttachmentsService().list().send().attachments();
        List<Nic> nicList = system.vmsService().vmService(id).nicsService().list().send().nics();
        Vm vm = system.vmsService().vmService(id).get().send().vm();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        return snapList.stream()
                .map(snapshot -> {
                    if (snapshot.vmPresent()) {  // 스냅샷 생성
                        List<Disk> diskList = system.vmsService().vmService(id).snapshotsService().snapshotService(snapshot.id()).disksService().list().send().disks();
                        List<Nic> snapnicList = system.vmsService().vmService(id).snapshotsService().snapshotService(snapshot.id()).nicsService().list().send().nics();

                        return SnapshotVo.builder()
                                .id(snapshot.id())
                                .description(snapshot.description())  // 스냅샷 이름
                                .date(sdf.format(snapshot.date().getTime()))
                                .status(snapshot.snapshotStatus().value())
                                .persistMemorystate(snapshot.persistMemorystate()) // 스냅샷에 메모리가 포함되어있다

                                .setMemory(snapshot.vm().memory())
                                .guaranteedMemory(snapshot.vm().memoryPolicy().guaranteed())
                                .cpuCore(vm.cpu().topology().coresAsInteger() * vm.cpu().topology().socketsAsInteger() * vm.cpu().topology().threadsAsInteger())

                                .sDiskList(
                                        diskList.stream()
                                                .map(disk -> {
                                                    DiskAttachment diskAttachment = system.vmsService().vmService(id).diskAttachmentsService().attachmentService(disk.id()).get().send().attachment();
                                                    return SnapshotDiskVo.builder()
                                                            .status(disk.status())
                                                            .id(disk.id())
                                                            .alias(disk.alias())
                                                            .virtualSize(disk.provisionedSize())
                                                            .actualSize(disk.actualSize())
                                                            .sparse(disk.sparse() ? "sparse" : "?")
                                                            .diskInterface(diskAttachment.interface_())
//                                                            .date() // 생성일자
//                                                            .diskSnapId(disk.imageId()) // 디스크 스냅샷 아이디
                                                            .storageType(disk.storageType())
                                                            .description(disk.description())
                                                            .build();
                                                })
                                                .collect(Collectors.toList())
                                )
//                                .nicVoList(
//                                        snapnicList.stream()
//                                                .map(nic -> {
//                                                    return NicVo.builder()
//                                                            .name()
//                                                            .macAddress()
//                                                            .networkName()
//                                                            .vnicProfileVo()
//                                                            .rxSpeed()
//                                                            .txSpeed()
//
//                                                    .build();
//                                                })
//                                                .collect(Collectors.toList())
//                                )
                                .build();
                    } else {  // 스냅샷 기본 Acitve Vm
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
                                                            .diskSnapId(disk.imageId())
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

    @Override
    public CommonVo<Boolean> previewSnapshot(String id, String snapId) {
        return null;
    }

    @Override
    public CommonVo<Boolean> commitSnapshot(String id, String snapId) {
        return null;
    }

    @Override
    public CommonVo<Boolean> restoreSnapshot(String id, String snapId) {
        return null;
    }

    // 스냅샷 삭제
    @Override
    public CommonVo<Boolean> deleteSnapshot(String id, String snapId) {
        SystemService system = admin.getConnection().systemService();

        try{
            // TODO:HELP 에러처리
            system.vmsService().vmService(id).snapshotsService().snapshotService(snapId).remove().send();

            log.info("성공: 스냅샷 삭제");
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error(e.getMessage());
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> copySnapshot() {
        return null;
    }

    @Override
    public CommonVo<Boolean> addTemplate() {
        return null;
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

    // 선호도 그룹

//    // TODO 안된거임
//
//    @Override
//    public List<AffinityGroupVo> getAffinitygroup(String id) {
//        SystemService system = admin.getConnection().systemService();
//        Vm vm = system.vmsService().vmService(id).get().send().vm();
//        List<AffinityGroup> affinityGroupList = system.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().list().send().groups();
//
//        log.info("가상머신 선호도그룹 목록");
//        return affinityGroupList.stream()
//                .map(ag ->
//                        AffinityGroupVo.builder()
//                                .id(ag.id())
//                                .name(ag.name())
//                                .description(ag.description())
//                                .status(ag.broken())
//                                .priority(ag.priority().intValue())  // 우선순위
//                                .positive(ag.positivePresent() && ag.positive())
//                                .vmEnabled(ag.vmsRule().enabled())
//                                .vmPositive(ag.vmsRule().positive())
//                                .vmEnforcing(ag.vmsRule().enforcing())
//                                .hostEnabled(ag.hostsRule().enabled())
//                                .hostPositive(ag.hostsRule().positive())
//                                .hostEnforcing(ag.hostsRule().enforcing())
//                                .hostMembers(ag.hostsPresent() ? itAffinityService.getAgHostList(system, id, ag.id()) : null)
//                                .vmMembers(ag.vmsPresent() ? itAffinityService.getAgVmList(system, id, ag.id()) : null)
//                                .hostLabels(ag.hostLabelsPresent() ? itAffinityService.getLabelName(ag.hostLabels().get(0).id()) : null)
//                                .vmLabels(ag.vmLabelsPresent() ? itAffinityService.getLabelName(ag.vmLabels().get(0).id()) : null)
//                                .build())
//                .collect(Collectors.toList());
//    }

    // 선호도 레이블  안함
    

    // 게스트 정보
    @Override
    public GuestInfoVo getGuestInfo(String id) {
        SystemService system = admin.getConnection().systemService();
        Vm vm = system.vmsService().vmService(id).get().send().vm();

        GuestInfoVo gif = null;
        if (vm.guestOperatingSystemPresent()) {
            gif = GuestInfoVo.builder()
                    .architecture(vm.guestOperatingSystem().architecture())
                    .type(vm.guestOperatingSystem().family())
                    .kernalVersion(vm.guestOperatingSystem().kernel().version().fullVersion())
                    .os(vm.guestOperatingSystem().distribution() + " " + vm.guestOperatingSystem().version().major())
                    .guestTime(vm.guestTimeZone().name() + vm.guestTimeZone().utcOffset())
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





    // vm 상태확인
    private boolean expectStatus(VmService vmService, VmStatus expectStatus, long interval, long timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (true) {
            Vm currentVm = vmService.get().send().vm();
            VmStatus status = currentVm.status();

            if (status == expectStatus) {
                return true;
            } else if (System.currentTimeMillis() - startTime > timeout) {
                return false;
            }

            log.info("가상머신 상태: {}", status);
            Thread.sleep(interval);
        }
    }



}
