package com.itinfo.itcloud.controller.setting;

import com.itinfo.itcloud.service.ItUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final ItUserService itUserService;
}
