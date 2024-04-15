package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class NetworkImportVo {
    // 공급자는 무조건 한개 ovirt-provider-ovn
    private String id;      // 공급자 id
    private String name;    // 공급자 name

    private List<OpenstackVo> osVoList;
}

// 이게 네트워크 공급자로 설정되어 있는 네트워크들의 리스트가 뜨고
// 그 네트워크 리스트들을 데이터 센터에서 다 쓸수있게한다?