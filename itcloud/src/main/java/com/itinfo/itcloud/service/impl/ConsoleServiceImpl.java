package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.ConsoleVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItConsoleService;
import com.itinfo.itcloud.service.ItSystemPropertyService;
import com.itinfo.util.model.SystemPropertiesVo;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.services.VmGraphicsConsoleService;
import org.ovirt.engine.sdk4.services.VmGraphicsConsolesService;
import org.ovirt.engine.sdk4.types.GraphicsConsole;
import org.ovirt.engine.sdk4.types.GraphicsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsoleServiceImpl implements ItConsoleService {
    @Autowired  private AdminConnectionService admin;
    @Autowired  private ItSystemPropertyService systemPropertyService;

    @Override
    public ConsoleVo setDisplay(String id, ConsoleVo consoleVo) {
        SystemService system = admin.getConnection().systemService();
        SystemPropertiesVo systemProperties = this.systemPropertyService.searchSystemProperties();
        List<GraphicsConsole> consoleList = system.vmsService().vmService(id).graphicsConsolesService().list().current(true).send().consoles();
        VmGraphicsConsolesService consolesService = system.vmsService().vmService(id).graphicsConsolesService();

        GraphicsConsole graphicsConsole =
                consoleList.stream()
                    .filter(c -> consoleVo.getProtocol().equalsIgnoreCase("VNC") ? c.protocol() == GraphicsType.VNC : c.protocol() == GraphicsType.SPICE)
                    .findFirst()
                    .orElse(null);

        VmGraphicsConsoleService graphicsConsoleService = consolesService.consoleService(graphicsConsole.id());

        return ConsoleVo.builder()
                .address(graphicsConsole.address())
                .port(String.valueOf(graphicsConsole.port().intValue()))
                .passwd(graphicsConsoleService.ticket().send().ticket().value())
                .tlsPort(graphicsConsole.tlsPort() != null ? String.valueOf(graphicsConsole.tlsPort().intValue()) : null)
                .hostAddress(systemProperties.getVncIp())
                .hostPort(systemProperties.getVncPort())
                .build();
    }



}
