package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.ItStorageDiskService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
@RequiredArgsConstructor
public class StorageDiskController {
	private final ItStorageDiskService storageDiskSerivce;
}
