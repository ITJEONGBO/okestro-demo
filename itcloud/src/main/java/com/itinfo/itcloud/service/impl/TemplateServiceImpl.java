package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.OsVo;
import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.TemplateCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.TemplateBuilder;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.services.TemplateService;
import org.ovirt.engine.sdk4.services.TemplatesService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
public class TemplateServiceImpl implements ItTemplateService {
    @Autowired private AdminConnectionService admin;
    private final CommonService commonService = new CommonService();


    /**
     * 템플릿 목록
     * @return 템플릿 목록
     */
    @Override
    public List<TemplateVo> getList() {
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


//    public List<DiskVo> getDiskAtt(String vmId){
//        SystemService system = admin.getConnection().systemService();
//
//    }


    /**
     * 템플릿 생성
     * @param vmId
     * @return
     */
    @Override
    public CommonVo<Boolean> addTemplate(String vmId, TemplateVo templateVo){
        SystemService system = admin.getConnection().systemService();
        TemplatesService tempsService = system.templatesService();

        try{
            TemplateBuilder templateBuilder = new TemplateBuilder();
            templateBuilder
                    .name(templateVo.getName())
                    .description(templateVo.getDescription());
//                    .diskAttachments()

            tempsService.add().template(templateBuilder.build()).send();
        }catch (Exception e){
            log.error("템플릿 생성 실패");
            return CommonVo.failResponse("템플릿 생성 실패");
        }

        return null;
    }



    /**
     * 템플릿 편집 창
     * @param id 템플릿 id
     * @return
     */
    @Override
    public TemplateCreateVo setEditTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        Template template = system.templatesService().templateService(id).get().send().template();


        return null;
    }

    /**
     * 템플릿 편집
     * @param tVo 템플릿 객체
     * @return
     */
    @Override
    public CommonVo<Boolean> editTemplate(TemplateCreateVo tVo) {
        return null;
    }

    /**
     * 템플릿 삭제
     * @param id 템플릿 id
     * @return
     */
    @Override
    public CommonVo<Boolean> deleteTemplate(String id) {
        return null;
    }


    /**
     * 템플릿 정보
     * @param id 템플릿 id
     * @return
     */
    @Override
    public TemplateVo getInfo(String id) {
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


    @Override
    public List<VmVo> getVm(String id) {
        SystemService system = admin.getConnection().systemService();
        log.info("템플릿 가상머신 목록");
        return system.vmsService().list().send().vms().stream()
                .filter(vm -> vm.templatePresent() && vm.template().id().equals(id))
                .map(vm ->
                        VmVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .hostName(vm.hostPresent() ? system.hostsService().hostService(vm.host().id()).get().send().host().name() : null)
                                .ipv4(commonService.getVmIp(system, vm.id(), "v4"))
                                .ipv6(commonService.getVmIp(system, vm.id(), "v6"))
                                .fqdn(vm.fqdnPresent() ? vm.fqdn() : null)
                                .status(vm.status().value())
                                .upTime(commonService.getVmUptime(system, vm.id()))
                                .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * 템플릿 네트워크 인터페이스 목록
     * @param id
     * @return
     */
    @Override
    public List<NicVo> getNic(String id) {
        SystemService system = admin.getConnection().systemService();

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
     * 템플릿 디스크 정보
     * @param id
     * @return
     */
    @Override
    public List<DiskVo> getDisk(String id) {
        SystemService system = admin.getConnection().systemService();
        TemplateService templateService = system.templatesService().templateService(id);

        return templateService.diskAttachmentsService().list().send().attachments().stream()
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
     *
     * @param id
     * @return
     */
    @Override
    public List<DomainVo> getDomain(String id) {
        SystemService system = admin.getConnection().systemService();
        DiskAttachment diskAtt = system.templatesService().templateService(id).diskAttachmentsService().list().follow("disk").send().attachments().get(0);
        Disk disk = system.disksService().diskService(diskAtt.id()).get().send().disk();
        StorageDomain domain = system.storageDomainsService().storageDomainService(disk.storageDomains().get(0).id()).get().send().storageDomain();

//        return DomainVo.builder()
//                .name(domain.name())
//                .domainType(domain.type())
//                .active(diskAtt.active())
//                .availableSize(domain.available())
//                .usedSize(domain.used())
//                .diskSize(domain.used().add(domain.available()))
        return null;
    }




    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Permission> permissionList = system.clustersService().clusterService(id).permissionsService().list().send().permissions();

        return commonService.getPermission(system, permissionList);
    }


    /**
     * 템플릿 이벤트 목록
     * @param id
     * @return
     */
    @Override
    public List<EventVo> getEvent(String id) {
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











}
