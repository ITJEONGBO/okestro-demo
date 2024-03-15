package com.itinfo.itcloud.model.network;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class NetworkImportVo {
    private String id;      // 공급자 id
    private String name;    // 공급자 name

    private List<OpenstackVo> osVoList;
    private List<DataCenterVo> dcVoList;

    private boolean permission;

}
