package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItDataCenterService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.DataCenterBuilder;
import org.ovirt.engine.sdk4.builders.VersionBuilder;
import org.ovirt.engine.sdk4.services.DataCenterService;
import org.ovirt.engine.sdk4.services.DataCentersService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.DataCenter;
import org.ovirt.engine.sdk4.types.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataCenterServiceImpl implements ItDataCenterService {
    @Autowired private AdminConnectionService admin;


    // 데이터센터 리스트 불러오기
    @Override
    public List<DataCenterVo> getList(){
        SystemService system = admin.getConnection().systemService();
        List<DataCenter> dataCenterList = system.dataCentersService().list().send().dataCenters();

        log.info("데이터 센터 리스트 출력");
        return dataCenterList.stream()
                .map(dataCenter ->
                    DataCenterVo.builder()
                        .id(dataCenter.id())
                        .name(dataCenter.name())
                        .description(dataCenter.description())
                        .storageType(dataCenter.local())
                        .status(TypeExtKt.findDCStatus(dataCenter.status()))
                        .quotaMode(TypeExtKt.findQuota(dataCenter.quotaMode()))
                        .version(dataCenter.version().major() + "." + dataCenter.version().minor())
                        .comment(dataCenter.comment())
                    .build()
                )
                .collect(Collectors.toList());
    }


    // 데이터센터 생성
    @Override
    public CommonVo<Boolean> addDatacenter(DataCenterCreateVo dcVo) {
        SystemService system = admin.getConnection().systemService();
        DataCentersService datacentersService = system.dataCentersService();     // datacenters 서비스 불러오기

        // 중복 확인 코드
        boolean nameDuplicate = datacentersService.list().search("name="+dcVo.getName()).send().dataCenters().isEmpty();
        String[] ver = dcVo.getVersion().split("\\.");      // 버전값 분리

        try {
            if(nameDuplicate) {
                DataCenter dataCenter = new DataCenterBuilder()
                        .name(dcVo.getName())       // 이름
                        .description(dcVo.getDescription())     // 설명
                        .local(dcVo.isStorageType())    // 스토리지 유형
                        .version(
                            new VersionBuilder()
                                .major(Integer.parseInt(ver[0]))
                                .minor(Integer.parseInt(ver[1]))
                            .build()
                        )  // 호환 버전
                        .quotaMode(dcVo.getQuotaMode())      // 쿼터 모드
                        .comment(dcVo.getComment())     // 코멘트
                    .build();

                datacentersService.add().dataCenter(dataCenter).send();     // 데이터센터 만든거 추가

                log.info("성공: 데이터센터 생성 {}", dataCenter.name());
                return CommonVo.createResponse();
            }else {
                log.error("실패: 데이터센터 이름 중복");
                return CommonVo.failResponse("실패: 데이터센터 이름 중복");
            }
        }catch (Exception e){
            log.error("실패: 데이터센터 생성 ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 데이터센터 - edit 시 필요한 값
    // 데이터센터 현재 설정되어있는 값 출력
    @Override
    public DataCenterCreateVo getDatacenter(String id){
        SystemService system = admin.getConnection().systemService();
        DataCenter dataCenter = system.dataCentersService().dataCenterService(id).get().send().dataCenter();

        log.info("데이터센터 {} 현재값 출력", dataCenter.name());
        return DataCenterCreateVo.builder()
                .id(id)
                .name(dataCenter.name())
                .description(dataCenter.description())
                .storageType(dataCenter.local())
                .quotaMode(dataCenter.quotaMode())
                .version(dataCenter.version().major() + "." + dataCenter.version().minor())
                .comment(dataCenter.comment())
                .build();
    }


    // 데이터센터 수정
    @Override
    public CommonVo<Boolean> editDatacenter(String id, DataCenterCreateVo dcVo) {
        SystemService system = admin.getConnection().systemService();
        DataCenterService dataCenterService = system.dataCentersService().dataCenterService(id);

        // 중복 이름 확인
        boolean nameDuplicate = system.dataCentersService().list().send().dataCenters().stream()
                        .filter(dataCenter -> !dataCenter.id().equals(id))
                        .anyMatch(dataCenter -> dataCenter.name().equals(dcVo.getName()));

        String[] ver = dcVo.getVersion().split("\\.");      // 버전값 분리

        try {
            if (!nameDuplicate) {
                DataCenter dataCenter =
                        new DataCenterBuilder()
                                .id(id)
                                .name(dcVo.getName())       // 이름
                                .description(dcVo.getDescription())     // 설명
                                .local(dcVo.isStorageType())    // 스토리지 유형
                                .version(
                                        new VersionBuilder()
                                                .major(Integer.parseInt(ver[0]))
                                                .minor(Integer.parseInt(ver[1]))
                                                .build()
                                )  // 호환 버전
                                .quotaMode(dcVo.getQuotaMode())      // 쿼터 모드
                                .comment(dcVo.getComment())     // 코멘트
                                .build();

                dataCenterService.update().dataCenter(dataCenter).send();   // 데이터센터 수정

                log.info("성공: 데이터센터 편집");
                return CommonVo.createResponse();
            }else {
                log.error("실패: 데이터센터 이름 중복");
                return CommonVo.failResponse("데이터센터 이름 중복");
            }
        }catch (Exception e) {
            log.error("실패: 데이터센터 편집 {}", e.getMessage());
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 데이터센터 삭제
    @Override
    public CommonVo<Boolean> deleteDatacenter(String id) {
        SystemService system = admin.getConnection().systemService();
        DataCenterService dataCenterService = system.dataCentersService().dataCenterService(id);
        String name = dataCenterService.get().send().dataCenter().name();

        try {
            dataCenterService.remove().force(true).send();

             log.info("성공: 데이터센터 {} 삭제", name);
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("실패: 데이터센터 {} 삭제 ", name, e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // 데이터센터 이벤트 출력
    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Event> eventList = system.eventsService().list().send().events();
        String dcName = system.dataCentersService().dataCenterService(id).get().send().dataCenter().name();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        log.info("데이터센터 {} 이벤트 출력", dcName);
        return eventList.stream()
                .filter(event -> event.dataCenterPresent()
                        && (event.dataCenter().idPresent() && event.dataCenter().id().equals(id) || (event.dataCenter().namePresent() && event.dataCenter().name().equals(dcName)) )
                )
                .map(event ->
                        EventVo.builder()
                            .datacenterName(dcName)
                            .severity(TypeExtKt.findLogSeverity(event.severity()))   //상태
                            .time(sdf.format(event.time()))
                            .message(event.description())
                            .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                            .source(event.origin())
                            .build()
                )
                .collect(Collectors.toList());
    }

}
