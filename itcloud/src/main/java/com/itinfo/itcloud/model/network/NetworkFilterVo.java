package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class NetworkFilterVo {
    private String id;
    private String name;
    private String value; // 값
}
