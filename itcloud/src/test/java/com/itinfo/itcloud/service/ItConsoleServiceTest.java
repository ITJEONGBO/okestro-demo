package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.ConsoleVo;
import org.junit.jupiter.api.Test;
import org.ovirt.engine.sdk4.types.GraphicsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItConsoleServiceTest {
    @Autowired ItConsoleService consoleService;

    @Test
    void name() {
        String vmId = "eec63849-5026-482c-8f05-1d8e419ef548";

        ConsoleVo consoleVo =
                ConsoleVo.builder()
                        .vmId(vmId)
                        .protocol(String.valueOf(GraphicsType.VNC))
                        .build();

        consoleService.setDisplay(vmId, consoleVo);
    }
}