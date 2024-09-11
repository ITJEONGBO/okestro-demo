package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.AssignedPermissionsService
import org.ovirt.engine.sdk4.services.StorageDomainsService
import org.ovirt.engine.sdk4.services.StorageDomainService
import org.ovirt.engine.sdk4.services.FilesService
import org.ovirt.engine.sdk4.services.FileService
import org.ovirt.engine.sdk4.services.StorageDomainDisksService
import org.ovirt.engine.sdk4.services.DiskSnapshotsService
import org.ovirt.engine.sdk4.services.StorageDomainTemplatesService
import org.ovirt.engine.sdk4.types.*

private fun Connection.srvStorageDomains(): StorageDomainsService =
	this.systemService.storageDomainsService()

fun Connection.findAllStorageDomains(searchQuery: String = ""): Result<List<StorageDomain>> = runCatching {
	if (searchQuery.isNotEmpty())
		srvStorageDomains().list().search(searchQuery).send().storageDomains()
	else
		srvStorageDomains().list().send().storageDomains()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccess("목록조회")
}.onFailure {
	Term.STORAGE_DOMAIN.logFail("목록조회", it)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvStorageDomain(storageDomainId: String): StorageDomainService =
	this.srvStorageDomains().storageDomainService(storageDomainId)

fun Connection.findStorageDomain(storageDomainId: String): Result<StorageDomain?> = runCatching {
	this.srvStorageDomain(storageDomainId).get().send().storageDomain()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccess("상세조회")
}.onFailure {
	Term.STORAGE_DOMAIN.logFail("상세조회", it)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.findStorageDomainName(storageDomainId: String): String
	= this.findStorageDomain(storageDomainId).getOrNull()?.name() ?: ""

fun Connection.addStorageDomain(storageDomain: StorageDomain): Result<StorageDomain?> = runCatching {
	val storageAdded: StorageDomain? =
		this.srvStorageDomains().add().storageDomain(storageDomain).send().storageDomain()

	storageAdded ?: throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccess("생성")
}.onFailure {
	Term.STORAGE_DOMAIN.logFail("생성", it)
	throw if (it is Error) it.toItCloudException() else it
}

// 도메인 관리
fun Connection.updateStorageDomain(storageDomainId: String, storageDomain: StorageDomain): Result<StorageDomain?> = runCatching {
	val storageDomainUpdated: StorageDomain? =
		this.srvStorageDomain(storageDomainId).update().storageDomain(storageDomain).send().storageDomain()

	storageDomainUpdated ?: throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccess("편집", storageDomainId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFail("편집", it, storageDomainId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.removeStorageDomain(storageId: String, format: Boolean = false): Result<Boolean> = runCatching {
	if(this.findStorageDomain(storageId).isFailure) {
		throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()
	}
	this.srvStorageDomain(storageId).remove().destroy(true).format(format)
	true
	// TODO EXpectStorageDomainDeleted()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccess("삭제", storageId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFail("삭제", it, storageId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvPermissionsFromStorageDomain(sdId: String): AssignedPermissionsService =
	this.srvStorageDomain(sdId).permissionsService()

fun Connection.findAllPermissionsFromStorageDomain(storageDomainId: String): Result<List<Permission>> = runCatching {
	if(this.findStorageDomain(storageDomainId).isFailure) {
		throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()
	}
	this.srvPermissionsFromStorageDomain(storageDomainId).list().send().permissions() ?: listOf()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccessWithin(Term.PERMISSION, "목록조회", storageDomainId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFailWithin(Term.PERMISSION, "목록조회", it, storageDomainId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvAllFilesFromStorageDomain(sdId: String): FilesService =
	this.srvStorageDomain(sdId).filesService()

fun Connection.findAllFilesFromStorageDomain(storageDomainId: String): Result<List<File>> = runCatching {
	this.srvAllFilesFromStorageDomain(storageDomainId).list().send().file()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccessWithin(Term.FILE, "목록조회", storageDomainId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFailWithin(Term.FILE, "목록조회", it, storageDomainId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvFileFromStorageDomain(storageId: String, fileId: String): FileService =
	this.srvAllFilesFromStorageDomain(storageId).fileService(fileId)

fun Connection.findFileFromStorageDomain(storageDomainId: String, fileId: String): Result<File?> = runCatching {
	this.srvFileFromStorageDomain(storageDomainId, fileId).get().send().file()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccessWithin(Term.FILE, "상세조회", storageDomainId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFailWithin(Term.FILE, "상세조회", it, storageDomainId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvDisksFromStorageDomain(storageId: String): StorageDomainDisksService =
	this.srvStorageDomain(storageId).disksService()

fun Connection.findAllDisksFromStorageDomain(storageDomainId: String): Result<List<Disk>> = runCatching {
	if(this.findStorageDomain(storageDomainId).isFailure) {
		throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()
	}
	this.srvDisksFromStorageDomain(storageDomainId).list().send().disks() ?: listOf()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccessWithin(Term.DISK, "목록조회", storageDomainId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFailWithin(Term.DISK, "목록조회", it, storageDomainId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvAllDiskSnapshotsFromStorageDomain(storageId: String): DiskSnapshotsService =
	this.srvStorageDomain(storageId).diskSnapshotsService()

fun Connection.findAllDiskSnapshotsFromStorageDomain(storageDomainId: String): Result<List<DiskSnapshot>> = runCatching {
	this.srvAllDiskSnapshotsFromStorageDomain(storageDomainId).list().send().snapshots()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccessWithin(Term.DISK_SNAPSHOT, "목록조회", storageDomainId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFailWithin(Term.DISK_SNAPSHOT, "목록조회", it, storageDomainId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvTemplatesFromStorageDomain(storageId: String): StorageDomainTemplatesService =
	this.srvStorageDomain(storageId).templatesService()

fun Connection.findAllTemplatesFromStorageDomain(storageDomainId: String): Result<List<Template>> = runCatching {
	this.srvTemplatesFromStorageDomain(storageDomainId).list().send().templates()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccessWithin(Term.TEMPLATE, "목록조회", storageDomainId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFailWithin(Term.TEMPLATE, "목록조회", it, storageDomainId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.findAllDiskProfilesFromStorageDomain(storageDomainId: String): Result<List<DiskProfile>> = runCatching {
	if(this.findStorageDomain(storageDomainId).isFailure){
		throw ErrorPattern.STORAGE_DOMAIN_NOT_FOUND.toError()
	}
	this.srvStorageDomain(storageDomainId).diskProfilesService().list().send().profiles() ?: listOf()
}.onSuccess {
	Term.STORAGE_DOMAIN.logSuccessWithin(Term.DISK_PROFILE, "목록조회", storageDomainId)
}.onFailure {
	Term.STORAGE_DOMAIN.logFailWithin(Term.DISK_PROFILE, "목록조회", it, storageDomainId)
	throw if (it is Error) it.toItCloudException() else it
}