package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class OpenstackVo {
    private String id;
    private String name;
    private String dcId;
}
