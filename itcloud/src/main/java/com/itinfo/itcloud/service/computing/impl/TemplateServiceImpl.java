package com.itinfo.itcloud.service.computing.impl;

import com.itinfo.itcloud.model.OsVo;
import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.TemplateCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.computing.ItTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
public class TemplateServiceImpl implements ItTemplateService {
    @Autowired private AdminConnectionService admin;
    @Autowired private CommonService common;


    /**
     * 템플릿 목록
     * @return 템플릿 목록
     */
    @Override
    public List<TemplateVo> getTemplates() {
        SystemService system = admin.getConnection().systemService();
        return system.templatesService().list().send().templates().stream()
                .map(template ->
                        TemplateVo.builder()
                            .id(template.id())
                            .name(template.name())
                            .versionName(template.versionPresent() ? template.version().versionName() : "")
                            .versionNum(template.versionPresent() ? template.version().versionNumberAsInteger() : 0)
                            .createDate(new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(template.creationTime().getTime()))
                            .status(template.status().value())
                            .clusterId(template.clusterPresent() ? template.cluster().id() : null)
                            .clusterName(template.clusterPresent() ? system.clustersService().clusterService(template.cluster().id()).get().send().cluster().name() : null)
                            .datacenterId(template.clusterPresent() ? system.clustersService().clusterService(template.cluster().id()).get().send().cluster().dataCenter().id() : null)
                            .datacenterName(template.clusterPresent() ?
                                    system.dataCentersService().dataCenterService(system.clustersService().clusterService(template.cluster().id()).get().send().cluster().dataCenter().id()).get().send().dataCenter().name() : null)
                            .description(template.description())
                            .build()
                )
                .collect(Collectors.toList());
    }



    /**
     * 템플릿 생성시 필요한 클러스터 리스트
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
//                                .datacenterId(cluster.dataCenter().id())
                                .datacenterName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                                .build()
                )
                .collect(Collectors.toList());
    }

//    @Override
//    public List<DiskVo> setVmDisk(String vmId){
//        SystemService system = admin.getConnection().systemService();
//        Vm vm = system.vmsService().vmService(vmId).get().send().vm();
//
//
//        return null;
//    }




    /**
     * 템플릿 생성 (Vm Status != up)
     *
     * 템플릿 디스크 붙이는 작업 (if, 도메인 용량이 초과 되었을 때, ovirt에서 알아서 다른 도메인을 설정해주는지는 알 수 없음)
     * 이부분은 나중에
     * @param vmId
     * @return
     */
    @Override
    public CommonVo<Boolean> addTemplate(String vmId, TemplateVo templateVo){
        SystemService system = admin.getConnection().systemService();
        TemplatesService templatesService = system.templatesService();

        try{
            if(system.vmsService().vmService(vmId).get().send().vm().status() == VmStatus.UP){
                log.debug("가상머신 up 상태에서는 템플릿 생성 불가");
                return CommonVo.failResponse("가상머신 UP 상태에서는 템플릿 생성 불가");
            }

            if (nameDuplicate(system, templateVo.getName(), null)) {
                log.error("템플릿 이름 중복");
                return CommonVo.duplicateResponse();
            }

            TemplateBuilder templateBuilder = addTemplateBuilder(templateVo);
            Template template = templatesService.add().template(templateBuilder.build()).send().template();

            TemplateService templateService = templatesService.templateService(template.id());
            if(expectTemplateStatus(templateService, TemplateStatus.OK, 1000, 30000)){
                log.info("템플릿 생성 완료");
                return CommonVo.createResponse();
            }else {
                log.error("템플릿 생성 시간초과");
                return CommonVo.failResponse("시간 초과");
            }
        }catch (Exception e){
            log.error("템플릿 생성 실패");
            return CommonVo.failResponse("템플릿 생성 실패");
        }
    }


    /**
     * 템플릿 편집 창
     * @param id 템플릿 id
     * @return
     */
    @Override
    public TemplateVo setTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        Template template = system.templatesService().templateService(id).get().send().template();

        return TemplateVo.builder()
                .id(id)
                .osType(template.osPresent() ? TypeExtKt.findOs(OsVo.valueOf(template.os().type())) : null)
                .chipsetFirmwareType(template.bios().typePresent() ? TypeExtKt.findBios(template.bios().type()) : null)
                .optimizeOption(TypeExtKt.findVmType(template.type())) // 최적화 옵션
                .versionName(template.version().versionName())
                .name(template.name())
                .description(template.description())
                .build();
    }

    /**
     * 템플릿 편집
     * @param tVo 템플릿 객체
     * @return
     */
    @Override
    public CommonVo<Boolean> editTemplate(TemplateVo tVo) {
        SystemService system = admin.getConnection().systemService();
        TemplateService templateService = system.templatesService().templateService(tVo.getId());

        try {
            if (nameDuplicate(system, tVo.getName(), null)) {
                log.error("템플릿 이름 중복");
                return CommonVo.duplicateResponse();
            }

            TemplateBuilder templateBuilder = editTemplateBuilder(tVo);
            templateService.update().template(templateBuilder.build()).send().template();

            log.info("템플릿 편집");
            return CommonVo.createResponse();
        }catch (Exception e){
            log.error("템플릿 편집 실패");
            return CommonVo.failResponse("템플릿 편집 실패");
        }
    }

    /**
     * 템플릿 삭제
     * @param id 템플릿 id
     * @return
     */
    @Override
    public CommonVo<Boolean> deleteTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        TemplateService templateService = system.templatesService().templateService(id);

        try {
            templateService.remove().send();
            log.info("템플릿 삭제");
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("템플릿 삭제 실패, {}", e.getMessage());
            return CommonVo.failResponse("템플릿 삭제 실패" + e.getMessage());
        }
    }


    /**
     * 템플릿 정보
     * @param id 템플릿 id
     * @return
     */
    @Override
    public TemplateVo getTemplateInfo(String id) {
        SystemService system = admin.getConnection().systemService();
        Template template = system.templatesService().templateService(id).get().send().template();

        // 상태 비저장
        return TemplateVo.builder()
                .id(template.id())
                .name(template.name())
                .description(template.description())
                .osType(template.osPresent() ? TypeExtKt.findOs(OsVo.valueOf(template.os().type())) : null)
                .chipsetFirmwareType(template.bios().typePresent() ? TypeExtKt.findBios(template.bios().type()) : null)
                .optimizeOption(TypeExtKt.findVmType(template.type())) // 최적화 옵션
                .memory(template.memoryPolicy().guaranteed())
                .cpuCoreCnt(template.cpu().topology().coresAsInteger())
                .cpuSocketCnt(template.cpu().topology().socketsAsInteger())
                .cpuThreadCnt(template.cpu().topology().threadsAsInteger())
                .cpuCnt(template.cpu().topology().coresAsInteger() * template.cpu().topology().socketsAsInteger() * template.cpu().topology().threadsAsInteger())
                .monitor(template.display().monitorsAsInteger())
                .ha(template.highAvailability().enabled())
                .priority(template.highAvailability().priorityAsInteger())
                .usb(template.usb().enabled())
                .hostCluster(template.clusterPresent() ? system.clustersService().clusterService(template.cluster().id()).get().send().cluster().name() : null)
                .origin(template.origin())
            .build();
    }


    /**
     * 템플릿 가상머신 목록
     * @param id 템플릿 id
     * @return 가상머신 목록
     */
    @Override
    public List<VmVo> getVmsByTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        log.info("템플릿 가상머신 목록");
        return system.vmsService().list().send().vms().stream()
                .filter(vm -> vm.templatePresent() && vm.template().id().equals(id))
                .map(vm ->
                        VmVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .hostName(vm.hostPresent() ? system.hostsService().hostService(vm.host().id()).get().send().host().name() : null)
                                .ipv4(common.getVmIp(system, vm.id(), "v4"))
                                .ipv6(common.getVmIp(system, vm.id(), "v6"))
                                .fqdn(vm.fqdnPresent() ? vm.fqdn() : null)
                                .status(vm.status().value())
                                .upTime(common.getVmUptime(system, vm.id()))
                                .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * 템플릿 네트워크 인터페이스 목록
     * @param id 템플릿 id
     * @return 네트워크 인터페이스 목록
     */
    @Override
    public List<NicVo> getNicsByTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        log.info("템플릿 네트워크 인터페이스 목록");
        return system.templatesService().templateService(id).nicsService().list().send().nics().stream()
                .map(nic -> {
                    VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
                    return NicVo.builder()
                            .id(nic.id())
                            .name(nic.name())
                            .linkStatus(nic.linked())
                            .plugged(nic.plugged())   // 연결됨
                            .networkName(system.networksService().networkService(vnicProfile.network().id()).get().send().network().name())
                            .vnicProfileVo(
                                    VnicProfileVo.builder()
                                            .id(vnicProfile.id())
                                            .name(vnicProfile.name())
                                            .build()
                            )
                            // 링크상태
                            .interfaces(nic.interface_().value())
                            .build();
                })
                .collect(Collectors.toList());
    }




    /**
     * 템플릿 디스크 목록
     * @param id 템플릿 id
     * @return 디스크 목록
     */
    @Override
    public List<DiskVo> getDisksByTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        log.info("템플릿 디스크 목록");
        return system.templatesService().templateService(id).diskAttachmentsService().list().send().attachments().stream()
                .filter(DiskAttachment::diskPresent)
                .map(diskAttachment -> {
                    Disk disk = system.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
                    StorageDomain sd = system.storageDomainsService().storageDomainService(disk.storageDomains().get(0).id()).get().send().storageDomain();
                    return DiskVo.builder()
                            .id(disk.id())
                            .name(disk.name())
                            .virtualSize(disk.provisionedSize())
                            .actualSize(disk.actualSize())  // 1보다 작은거 처리는 front에서
                            .status(disk.status())
                            .sparse(disk.sparse())
                            .diskInterface(diskAttachment.interface_())
                            .storageType(TypeExtKt.findStorageType(disk.storageType()))
//                            .createDate(disk.)
                            .domainVo(
                                    DomainVo.builder()
                                            .name(sd.name())
                                            .active(diskAttachment.active())
                                            .domainType(sd.type()) // 마스터
                                            .domainTypeMaster(sd.master() /*? "마스터" : ""*/)
                                            .usedSize(sd.used())
                                            .availableSize(sd.available())
                                            .diskSize(sd.used().add(sd.available()))
                                            .build()
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }


    /**
     * 템플릿 스토리지 도메인 목록
     * @param id 템플릿 id
     * @return 스토리지 도메인 목록
     */
    // TODO: 스토리지 도메인 묶는 작업 못함
    @Override
    public List<DomainVo> getDomainsByTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        TemplateDiskAttachmentsService dasService = system.templatesService().templateService(id).diskAttachmentsService();
        List<DiskAttachment> diskAttachmentList = dasService.list().send().attachments();

        return diskAttachmentList.stream()
                .map(da -> {
                    String diskId = dasService.attachmentService(da.id()).get().send().attachment().disk().id();
                    Disk disk = system.disksService().diskService(diskId).get().send().disk();
                    StorageDomain domain = system.storageDomainsService().storageDomainService(disk.storageDomains().get(0).id()).get().send().storageDomain();

                    List<DiskVo> diskVoList = new ArrayList<>();
                    diskVoList.add(
                            DiskVo.builder()
                                .id(diskId)
                                .name(disk.name())
                            .build()
                    );

                    return DomainVo.builder()
                            .name(domain.name())
                            .domainType(domain.type())
//                            .active(disk.active())
                            .availableSize(domain.available())
                            .usedSize(domain.used())
                            .diskSize(domain.used().add(domain.available()))
                            .diskVoList(diskVoList)
                            .build();
                })
                .collect(Collectors.toList());
    }


    /**
     * 템플릿 권한 목록
     * @param id 템플릿 id
     * @return 권한 목록
     */
    @Override
    public List<PermissionVo> getPermissionsByTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Permission> permissionList = system.clustersService().clusterService(id).permissionsService().list().send().permissions();

        return common.getPermission(system, permissionList);
    }


    /**
     * 템플릿 이벤트 목록
     * @param id 템플릿 id
     * @return 이벤트 목록
     */
    @Override
    public List<EventVo> getEventsByTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        Template t = system.templatesService().templateService(id).get().send().template();

        return system.eventsService().list().send().events().stream()
                .filter(event -> event.templatePresent() && event.template().name().equals(t.name()))
                .map(event ->
                        EventVo.builder()
                            .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                            .time(new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss").format(event.time()))
                            .message(event.description())
                            .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                            .source(event.origin())
                        .build()
                )
                .collect(Collectors.toList());
    }




    /**
     * 템플릿 생성 빌더
     * @param tVo 템플릿
     * @return 템플릿 빌더
     */
    private TemplateBuilder addTemplateBuilder(TemplateVo tVo){
        return new TemplateBuilder()
                .name(tVo.getName())
                .description(tVo.getDescription())
                .cluster(new ClusterBuilder().id(tVo.getClusterId()));
    }


    /**
     * 템플릿 수정 빌더
     * @param tVo 템플릿
     * @return 템플릿 빌더
     */
    private TemplateBuilder editTemplateBuilder(TemplateVo tVo){
        return new TemplateBuilder()
                .id(tVo.getId())
                .name(tVo.getName())
                .description(tVo.getDescription())
                .os(new OperatingSystemBuilder().type(tVo.getOsType()))
                .bios(new BiosBuilder().type(BiosType.valueOf(tVo.getChipsetFirmwareType())))
                .cluster(new ClusterBuilder().id(tVo.getClusterId()))
                .type(VmType.valueOf(tVo.getOptimizeOption()));
    }

    /**
     * 템플릿 상태확인
     * @param templateService
     * @param expectStatus
     * @param interval
     * @param timeout
     * @return
     * @throws InterruptedException
     */
    private boolean expectTemplateStatus(TemplateService templateService, TemplateStatus expectStatus, long interval, long timeout) throws InterruptedException{
        long startTime = System.currentTimeMillis();
        while(true){
            Template currentTmp = templateService.get().send().template();
            TemplateStatus status = currentTmp.status();

            if(status == expectStatus){
                return true;
            }else if(System.currentTimeMillis() - startTime > timeout){
                return false;
            }
            log.debug("템플릿 상태: {}", status);
            Thread.sleep(interval);
        }
    }

    /**
     * 템플릿 이름 중복 확인
     * @param system
     * @param name
     * @param id
     * @return
     */
    private boolean nameDuplicate(SystemService system, String name, String id){
        return system.templatesService().list().send().templates().stream()
                .filter(template -> template.id() == null || !template.id().equals(id))
                .anyMatch(template -> template.name().equals(name));
    }



}
