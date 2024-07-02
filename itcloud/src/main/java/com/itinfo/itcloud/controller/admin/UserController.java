package com.itinfo.itcloud.controller.admin;

import com.itinfo.itcloud.service.admin.ItUserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Api(tags = "user")
@Slf4j
public class UserController {
	private final ItUserService itUserService;
}
