package com.itinfo.service;

import com.itinfo.model.DiskVo;
import com.itinfo.model.DiskCreateVo;
import com.itinfo.model.DiskMigrationVo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DisksService {
	List<DiskVo> retrieveDisks();

	List<DiskVo> retrieveDisks(String storageDomainName);

	void createDisk(DiskCreateVo diskCreateVo);

	void createLunDisk(DiskCreateVo diskCreateVo);

	void removeDisk(List<String> disks);

	void migrationDisk(DiskMigrationVo diskMigrationVo);

	void uploadDisk(byte[] bytes, DiskCreateVo diskCreateVo, InputStream is, long diskSize);

	String retrieveDiskImage(File file) throws IOException;
}
