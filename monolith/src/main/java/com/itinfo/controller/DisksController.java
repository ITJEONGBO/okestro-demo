package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.WebsocketService;
import com.itinfo.model.DiskCreateVo;
import com.itinfo.model.DiskVo;
import com.itinfo.model.DiskMigrationVo;
import com.itinfo.service.DisksService;

import java.io.InputStream;
import java.util.List;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@Api(value = "DisksController", tags = {"disks"})
public class DisksController extends BaseController {
	@Autowired private DisksService disksService;
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private WebsocketService websocketService;

	@ApiOperation(httpMethod = "GET", value = "disksView", notes = "페이지 이동 > /storage/disks")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/disks"})
	public String disksView() {
		log.info("... disksView");
		return "/castanets/storage/disks";
	}
	@ApiOperation(httpMethod = "GET", value = "createDiskView", notes = "페이지 이동 > /storage/createDisk")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/createDisk"})
	public String createDiskView() {
		log.info("... createDiskView");
		return "/castanets/storage/createDisk";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDisks", notes = "디스크 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/disks/retrieveDisks"})
	public String retrieveDisks(Model model) {
		log.info("... retrieveDisks");
		List<DiskVo> disks = disksService.retrieveDisks();
		model.addAttribute(ItInfoConstant.RESULT_KEY, disks);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "createDisk", notes = "디스크 생성")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/disks/createDisk"})
	public String createDisk(@RequestBody DiskCreateVo diskCreateVo, 
							 Model model) {
		log.info("... createDisk");
		disksService.createDisk(diskCreateVo);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "createLunDisk", notes = "LUN 디스크 생성")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/disks/createLunDisk"})
	public String createLunDisk(@RequestBody DiskCreateVo diskCreateVo,
								Model model) {
		log.info("... createLunDisk");
		disksService.createLunDisk(diskCreateVo);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "removeDisk", notes = "디스크 제거")
	@ApiImplicitParams({
			@ApiImplicitParam(name="diskIds", value = "제거할 디스크 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/disks/removeDisk"})
	public String removeDisk(@RequestBody List<String> diskIds,
							 Model model) {
		log.info("... removeDisk[{}]", diskIds.size());
		disksService.removeDisk(diskIds);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "migrationDisk", notes = "디스크 이관")
	@ApiImplicitParams({
			@ApiImplicitParam(name="diskMigrationVo", value = "이관할 디스크", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/disks/migrationDisk"})
	public String migrationDisk(@RequestBody DiskMigrationVo diskMigrationVo,
								Model model) {
		log.info("... migrationDisk");
		disksService.migrationDisk(diskMigrationVo);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "uploadDisk", notes = "디스크 이관")
	@ApiImplicitParams({
			@ApiImplicitParam(name="file", value = "업로드 할 디스크 파일", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@Async("karajanTaskExecutor")
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/disks/uploadDisk"})
	public String uploadDisk(@RequestParam("file") MultipartFile diskFile,
						   DiskCreateVo diskCreateVo,
						   Model model) {
		try {
			byte[] bytes = diskFile.getBytes();
			InputStream is = diskFile.getInputStream();
			long diskSize = diskFile.getSize();
			disksService.uploadDisk(bytes, diskCreateVo, is, diskSize);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.fillInStackTrace();
		}
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDiskImage", notes = "디스크 이미지 정보 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="file", value = "업로드 할 디스크 파일", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@Async("karajanTaskExecutor")
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/disks/retrieveDiskImage"})
	public String retrieveDiskImage(@RequestParam("file") MultipartFile diskFile){
		return ItInfoConstant.JSON_VIEW;
	}
}
