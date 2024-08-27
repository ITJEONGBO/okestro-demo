package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.DiskService
import org.ovirt.engine.sdk4.services.DisksService
import org.ovirt.engine.sdk4.types.Disk
import org.ovirt.engine.sdk4.types.DiskStatus
import org.ovirt.engine.sdk4.types.StorageDomain

private fun Connection.srvAllDisks(): DisksService =
	systemService.disksService()

fun Connection.findAllDisks(searchQuery: String = ""): Result<List<Disk>> = runCatching {
	if (searchQuery.isNotEmpty())
		this.srvAllDisks().list().search(searchQuery).caseSensitive(false).send().disks()
	else
		this.srvAllDisks().list().send().disks()
}.onSuccess {
	Term.DISK.logSuccess("목록조회")
}.onFailure {
	Term.DISK.logFail("목록조회")
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvDisk(diskId: String): DiskService =
	srvAllDisks().diskService(diskId)
fun Connection.findDisk(diskId: String): Result<Disk?> = runCatching {
	this.srvDisk(diskId).get().send().disk()
}.onSuccess {
	Term.DISK.logSuccess("상세조회")
}.onFailure {
	Term.DISK.logFail("상세조회")
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.addDisk(disk: Disk): Result<Disk?> = runCatching {
	val diskAdded: Disk = this.srvAllDisks().add().disk(disk).send().disk() ?: return Result.failure(Error("디스크 생성 실패 ..."))
	if (expectDiskStatus(diskAdded.id())) {
		log.error("디스크 생성 실패 ... 시간초과")
		return Result.failure(Error("디스크 생성 실패 ... 시간초과"))
	}
	diskAdded
}.onSuccess {
	Term.DISK.logSuccess("생성")
}.onFailure {
	Term.DISK.logFail("생성")
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.updateDisk(disk: Disk): Result<Disk?> = runCatching {
	this@updateDisk.findDisk(disk.id()).getOrNull() ?: throw ErrorPattern.DISK_NOT_FOUND.toError()
	this.srvDisk(disk.id()).update().disk(disk).send().disk()
}.onSuccess {
	Term.DISK.logSuccess("편집")
}.onFailure {
	Term.DISK.logFail("편집")
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.removeDisk(diskId: String): Result<Boolean> = runCatching {
	val disk: Disk = this@removeDisk.findDisk(diskId).getOrNull() ?: throw ErrorPattern.DISK_NOT_FOUND.toError()
	this.srvDisk(diskId).remove().send()
	val result: Boolean =
		this.findAllDisks()
			.getOrDefault(listOf())
			.isDiskDeleted(disk)
	result
}.onSuccess {
	Term.DISK.logSuccess("삭제")
}.onFailure {
	Term.DISK.logFail("삭제")
	throw if (it is Error) it.toItCloudException() else it
}


fun List<Disk>.isDiskDeleted(removeDisk: Disk, interval: Long = 1000L, timeout: Long = 60000L): Boolean {
	log.debug("isHostDeleted ... ")
	val startTime = System.currentTimeMillis()
	while (true) {
		val diskExists = this@isDiskDeleted.any { it.id() == removeDisk.id() }
		if (!diskExists) {// !(매치되는것이 있다)
			log.info("디스크 {} 삭제", removeDisk.name())
			return true
		} else if (System.currentTimeMillis() - startTime > timeout) {
			log.error("디스크 {} 삭제 시간 초과", removeDisk.name())
			return false
		}
		log.debug("디스크 삭제 진행중 ... ")
		Thread.sleep(interval)
	}
}


fun Connection.moveDisk(diskId: String, domainId: String): Result<Boolean> = runCatching {
	this@moveDisk.findDisk(diskId).getOrNull() ?: throw ErrorPattern.DISK_NOT_FOUND.toError()
	val storageDomain: StorageDomain = this@moveDisk.findStorageDomain(domainId).getOrNull() ?: throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()

	this.srvDisk(diskId).move().storageDomain(storageDomain).send()
	if (!this@moveDisk.expectDiskStatus(diskId)) {
		log.error("디스크 이동 실패 ... 시간 초과")
		return Result.failure(Error("디스크 이동 실패 ... 시간 초과"))
	}
	true
}.onSuccess {
	Term.DISK.logSuccess("이동")
}.onFailure {
	Term.DISK.logFail("이동")
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.copyDisk(diskId: String, domainId: String): Result<Boolean> = runCatching {
	val disk: Disk = this@copyDisk.findDisk(diskId).getOrNull() ?: throw ErrorPattern.DISK_NOT_FOUND.toError()
	val storageDomain: StorageDomain = this@copyDisk.findStorageDomain(domainId).getOrNull() ?: throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()
	this.srvDisk(diskId).copy().disk(disk).storageDomain(storageDomain).send() // 복사된 디스크가 같은 ID를 가질까?

	if (!this@copyDisk.expectDiskStatus(disk.id())) {
		log.error("디스크 복사 실패 ... 시간 초과")
		return Result.failure(Error("시간 초과"))
	}
	true
}.onSuccess {
	Term.DISK.logSuccess("복사")
}.onFailure {
	Term.DISK.logFail("복사")
	throw if (it is Error) it.toItCloudException() else it
}


/**
 * [Connection.expectDiskStatus]
 * 가상머신 생성 - 디스크 생성 상태확인
 *
 * @param diskService
 * @param expectStatus
 * @param interval
 * @param timeout
 * @return
 * @throws InterruptedException
 */
@Throws(InterruptedException::class)
fun Connection.expectDiskStatus(diskId: String, expectStatus: DiskStatus = DiskStatus.OK, timeout: Long = 90000L, interval: Long = 1000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val disk: Disk? = this@expectDiskStatus.findDisk(diskId).getOrNull()
		val status = disk?.status()
		if (status == expectStatus)
			return true
		else if (System.currentTimeMillis() - startTime > timeout)
			return false

		log.info("디스크 상태: {}", status)
		Thread.sleep(interval)
	}
}
