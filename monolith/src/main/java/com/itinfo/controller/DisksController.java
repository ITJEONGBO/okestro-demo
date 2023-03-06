package com.itinfo.controller;

import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.WebsocketService;
import com.itinfo.model.DiskCreateVo;
import com.itinfo.model.DiskVo;
import com.itinfo.model.DiskMigrationVo;
import com.itinfo.service.DisksService;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DisksController {
	@Autowired
	@Qualifier("disksServiceImpl")
	private DisksService disksService;

	@Autowired
	private AdminConnectionService adminConnectionService;

	@Autowired
	private WebsocketService websocketService;

	@RequestMapping({"/storage/disks"})
	public String disksView() { return "/castanets/storage/disks";  }
	@RequestMapping({"/storage/createDisk"})
	public String createDiskView() {    return "/castanets/storage/createDisk"; }

	@RequestMapping(value = {"/storage/disks/retrieveDisks"}, method = {RequestMethod.GET})
	public String retrievedisks(Model model) {
		List<DiskVo> disks = this.disksService.retrieveDisks();
		model.addAttribute("resultKey", disks);
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/disks/createDisk"}, method = {RequestMethod.POST})
	public String createDisk(@RequestBody DiskCreateVo diskCreateVo, Model model) {
		this.disksService.createDisk(diskCreateVo);
		try {
			Thread.sleep(500L);
		} catch (Exception exception) {
		}
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/disks/createLunDisk"}, method = {RequestMethod.POST})
	public String createLunDisk(@RequestBody DiskCreateVo diskCreateVo, Model model) {
		this.disksService.createLunDisk(diskCreateVo);
		try {
			Thread.sleep(500L);
		} catch (Exception exception) {
		}
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/disks/removeDisk"}, method = {RequestMethod.POST})
	public String removeDisk(@RequestBody List<String> disks, Model model) {
		this.disksService.removeDisk(disks);
		try {
			Thread.sleep(500L);
		} catch (Exception exception) {
		}
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/disks/migrationDisk"}, method = {RequestMethod.POST})
	public String migrationDisk(@RequestBody DiskMigrationVo diskMigrationVo, Model model) {
		this.disksService.migrationDisk(diskMigrationVo);
		try {
			Thread.sleep(500L);
		} catch (Exception exception) {
		}
		return "jsonView";
	}

	@Async("karajanTaskExecutor")
	@RequestMapping(value = {"/storage/disks/uploadDisk"}, method = {RequestMethod.POST})
	public void uploadDisk(@RequestParam("file") MultipartFile diskFile, DiskCreateVo diskCreateVo) throws Exception {
		try {
			byte[] bytes = diskFile.getBytes();
			InputStream is = diskFile.getInputStream();
			long diskSize = diskFile.getSize();
			this.disksService.uploadDisk(bytes, diskCreateVo, is, diskSize);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		try {
			Thread.sleep(500L);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}

	@Async("karajanTaskExecutor")
	@RequestMapping(value = {"/storage/disks/retrieveDiskImage"}, method = {RequestMethod.POST})
	public String retrieveDiskImage(@RequestParam("file") MultipartFile diskFile) throws Exception {
		return "jsonView";
	}
}
