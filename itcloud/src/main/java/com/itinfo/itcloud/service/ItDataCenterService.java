package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItDataCenterService {
    List<DataCenterVo> getList();

    CommonVo<Boolean> addDatacenter(DataCenterCreateVo dcVo);   // 생성
    DataCenterCreateVo getDatacenter(String id);
    CommonVo<Boolean> editDatacenter(String id, DataCenterCreateVo dcVo);   // 수정
    CommonVo<Boolean> deleteDatacenter(String id);       // 삭제

    List<EventVo> getEvent(String id);

}
