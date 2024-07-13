package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItDataCenterService {
    List<DataCenterVo> getDatacenterList();   // 목록

    CommonVo<Boolean> addDatacenter(DataCenterCreateVo dcVo);   // 생성
    DataCenterCreateVo setDatacenter(String id);    // 수정 창
    CommonVo<Boolean> editDatacenter(DataCenterCreateVo dcVo);  // 수정
    CommonVo<Boolean> deleteDatacenter(String id);  // 삭제

    List<EventVo> getDatacenterEventList(String id);  // 이벤트 출력

    List<DataCenterVo> setComputing();
    List<DataCenterVo> setNetwork();
    List<DataCenterVo> setStorage();
}
