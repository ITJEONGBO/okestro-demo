package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItDataCenterService {
    List<DataCenterVo> getDataCenters();   // 목록

    CommonVo<Boolean> addDatacenter(DataCenterCreateVo dcVo);   // 생성
    DataCenterCreateVo setDatacenter(String id);    // 수정 창
    CommonVo<Boolean> editDatacenter(DataCenterCreateVo dcVo);  // 수정
    CommonVo<Boolean> deleteDatacenter(List<String> ids);  // 삭제

    List<EventVo> getEventsByDatacenter(String id);  // 이벤트 출력

    List<DataCenterVo> dashboardComputing();
    List<DataCenterVo> dashboardNetwork();
    List<DataCenterVo> dashboardStorage();
}
