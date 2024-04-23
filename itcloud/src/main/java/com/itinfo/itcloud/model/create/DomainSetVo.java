package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.Host;

import java.util.List;

@Getter @Builder @ToString
public class DomainSetVo {
    private String dcId;
    private String dcName;
    private List<Host> hostList;
}
