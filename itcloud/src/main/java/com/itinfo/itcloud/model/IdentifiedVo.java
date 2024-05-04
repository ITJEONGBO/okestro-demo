package com.itinfo.itcloud.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class IdentifiedVo {
    private String id;
    private String name;
}
