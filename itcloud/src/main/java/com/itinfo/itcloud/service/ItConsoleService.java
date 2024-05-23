package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.ConsoleVo;
import org.springframework.stereotype.Service;

@Service
public interface ItConsoleService {
    ConsoleVo setDisplay(String id, ConsoleVo console);
}
