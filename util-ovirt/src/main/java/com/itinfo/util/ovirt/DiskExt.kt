package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.DiskBuilder
import org.ovirt.engine.sdk4.internal.containers.ImageContainer
import org.ovirt.engine.sdk4.internal.containers.ImageTransferContainer
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.*

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

fun Connection.findAllStorageDomainsFromDisk(diskId: String): Result<List<StorageDomain>> = runCatching {
	val disk: Disk =
		this.findDisk(diskId)
			.getOrNull() ?: throw ErrorPattern.DISK_NOT_FOUND.toError()
	val storageDomains: List<StorageDomain> =
		disk.storageDomains()

	storageDomains
}.onSuccess {
	Term.DISK.logSuccess("{} 목록조회", Term.STORAGE_DOMAIN.desc)
}.onFailure {
	Term.DISK.logFail("목록조회")
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.addDisk(disk: Disk): Result<Disk?> = runCatching {
	val diskAdded: Disk =
		this.srvAllDisks().add().disk(disk).send().disk() ?: throw ErrorPattern.DISK_NOT_FOUND.toError()
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
	this@removeDisk.findDisk(diskId).getOrNull() ?: throw ErrorPattern.DISK_ID_NOT_FOUND.toError()
	this.srvDisk(diskId).remove().send()
	this.expectDiskDeleted(diskId)
}.onSuccess {
	Term.DISK.logSuccess("삭제")
}.onFailure {
	Term.DISK.logFail("삭제")
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.expectDiskDeleted(diskId: String, interval: Long = 1000L, timeout: Long = 60000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val disks: List<Disk> =
			this.findAllDisks().getOrDefault(listOf())
		val diskToRemove: Disk? = disks.firstOrNull() {it.id() == diskId}
		if (diskToRemove == null) { // !(매치되는것이 있다)
			Term.DISK.logSuccess("삭제")
			return true
		} else if (System.currentTimeMillis() - startTime > timeout) {
			log.error("{} {} 삭제 실패 ... 시간 초과", Term.DISK.desc, diskToRemove)
			return false
		}
		log.debug("{} 삭제 진행중 ... ", Term.DISK.desc)
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

fun Connection.copyDisk(diskId: String, diskAlias:String, domainId: String): Result<Boolean> = runCatching {
	val disk: Disk =
		this.findDisk(diskId)
			.getOrNull() ?: throw ErrorPattern.DISK_NOT_FOUND.toError()
	val storageDomain: StorageDomain =
		this.findStorageDomain(domainId)
			.getOrNull() ?: throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()

	this.srvDisk(diskId).copy().disk(DiskBuilder().id(diskId).alias(diskAlias)).storageDomain(storageDomain).send()
	// 복사된 디스크가 같은 ID를 가질까? => x
	// 근데 복사시 복사대상이 되는 디스크도 같이 Lock 걸리고 같이 잠김

	if (!expectDiskStatus(disk.id())) {
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

private fun Connection.srvAllImageTransfer(): ImageTransfersService =
	systemService.imageTransfersService()

private fun Connection.findAllImageTransfers(): Result<List<ImageTransfer>> = runCatching {
	this.srvAllImageTransfer().list().send().imageTransfer()
}.onSuccess {
	Term.IMAGE_TRANSFER.logSuccess("목록조회")
}.onFailure {
	Term.IMAGE_TRANSFER.logFail("목록조회")
	throw if (it is Error) it.toItCloudException() else it
}


private fun Connection.srvImageTransfer(imageId: String): ImageTransferService =
	this.srvAllImageTransfer().imageTransferService(imageId)

private fun Connection.findImageTransfer(imageId: String): Result<ImageTransfer?> = runCatching {
	this.srvImageTransfer(imageId).get().send().imageTransfer()
}.onSuccess {
	Term.IMAGE_TRANSFER.logSuccess("상세조회")
}.onFailure {
	Term.IMAGE_TRANSFER.logFail("상세조회")
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.addImageTransfer(imageTransferContainer: ImageTransferContainer): Result<ImageTransfer?> = runCatching {
	this.srvAllImageTransfer().add().imageTransfer(imageTransferContainer).send().imageTransfer()
}.onSuccess {
	Term.IMAGE_TRANSFER.logSuccess("업로드")
}.onFailure {
	Term.IMAGE_TRANSFER.logFail("업로드")
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.uploadDisk(/*file: MultipartFile?, */disk: Disk): Result<Boolean> = runCatching {
	// 디스크 생성
	val diskUpload: Disk =
		this.addDisk(disk)
			.getOrNull() ?: throw ErrorPattern.DISK_NOT_FOUND.toError()

	this.expectDiskStatus(diskUpload.id())

	val imageContainer = ImageContainer()
	imageContainer.id(diskUpload.id())

	val imageTransferContainer = ImageTransferContainer()
	imageTransferContainer.direction(ImageTransferDirection.UPLOAD)
	imageTransferContainer.image(imageContainer)

	val imageTransfer: ImageTransfer =
		addImageTransfer(imageTransferContainer)
			.getOrNull() ?: throw ErrorPattern.IMAGE_TRANSFER_NOT_FOUND.toError()

	while(imageTransfer.phasePresent() && imageTransfer.phase() == ImageTransferPhase.INITIALIZING){
		log.debug("이미지 업로드 상태확인 ... {}", imageTransfer.phase())
		Thread.sleep(1000)
	}

	val transferUrl = imageTransfer.transferUrl()
	if(transferUrl != null) this.srvImageTransfer(imageTransfer.id()) // imageSend
	else throw ErrorPattern.OVIRTUSER_REQUIRED_VALUE_EMPTY.toError()
	true
}.onSuccess {
	Term.DISK.logSuccess("파일 업로드")
}.onFailure {
	Term.DISK.logFail("파일 업로드")
	throw if (it is Error) it.toItCloudException() else it
}


/**
 * [Connection.expectDiskStatus]
 * 가상머신 생성 - 디스크 생성 상태확인
 *
 * @param diskId
 * @param expectStatus
 * @param interval
 * @param timeout
 * @return
 * @throws InterruptedException
 */
@Throws(InterruptedException::class)
fun Connection.expectDiskStatus(diskId: String, expectStatus: DiskStatus = DiskStatus.OK, timeout: Long = 60000L, interval: Long = 1000L): Boolean {
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

private fun Connection.srvPermissionsFromDisk(diskId: String): AssignedPermissionsService =
	this.srvDisk(diskId).permissionsService()

fun Connection.findAllPermissionsFromDisk(diskId: String): Result<List<Permission>> = runCatching {
	this.srvPermissionsFromDisk(diskId).list().send().permissions()
}.onSuccess {
	Term.DISK.logSuccessWithin(Term.PERMISSION, "목록조회", diskId)
}.onFailure {
	Term.DISK.logFailWithin(Term.PERMISSION, "목록조회", it, diskId)
	throw if (it is Error) it.toItCloudException() else it
}
