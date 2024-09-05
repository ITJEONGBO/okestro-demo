package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.*

private fun Connection.srvVms(): VmsService =
	this.systemService.vmsService()

fun Connection.findAllVms(searchQuery: String = "", follow: String = ""): Result<List<Vm>> = runCatching {
	if (searchQuery.isNotEmpty() && follow.isNotEmpty())
		srvVms().list().allContent(true).search(searchQuery).follow(follow).caseSensitive(false).send().vms()
	else if (searchQuery.isNotEmpty())
		srvVms().list().allContent(true).search(searchQuery).caseSensitive(false).send().vms()
	else if (follow.isNotEmpty())
		srvVms().list().allContent(true).follow(follow).caseSensitive(false).send().vms()
	else
		srvVms().list().allContent(true).send().vms()
}.onSuccess {
	Term.VM.logSuccess("목록조회")
}.onFailure {
	Term.VM.logFail("목록조회", it)
	throw if (it is Error) it.toItCloudException() else it

}


fun Connection.srvVm(vmId: String): VmService =
	this.srvVms().vmService(vmId)

fun Connection.findVm(vmId: String, follow: String = ""): Result<Vm?> = runCatching {
	if (follow.isNotEmpty())
		this.srvVm(vmId).get().follow(follow).send().vm()
	else
		this.srvVm(vmId).get().send().vm()
}.onSuccess {
	Term.VM.logSuccess("상세조회", vmId)
}.onFailure {
	Term.VM.logFail("상세조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.findVmName(vmId: String): Result<String> = kotlin.runCatching {
	this.findVm(vmId).getOrNull()?.name() ?: ""
}.onSuccess {
	Term.VM.logSuccess("이름 조회")
}.onFailure {
	Term.VM.logFail("이름 조회", it)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.startVm(vmId: String): Result<Boolean> = runCatching {
	log.debug("Connection.startVm ... ")
	val vm: Vm = this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	this.srvVm(vmId).start().useCloudInit(vm.initializationPresent()).send()

	this@startVm.expectVmStatus(vmId, VmStatus.UP)
}.onSuccess {
	Term.VM.logSuccess("시작", vmId)
}.onFailure {
	Term.VM.logFail("시작", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.stopVm(vmId: String): Result<Boolean> = runCatching {
	log.debug("Connection.stopVm ... ")
	this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	this.srvVm(vmId).stop().send()

	this@stopVm.expectVmStatus(vmId, VmStatus.DOWN)
}.onSuccess {
	Term.VM.logSuccess("전원끄기", vmId)
}.onFailure {
	Term.VM.logFail("전원끄기", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.suspendVm(vmId: String): Result<Boolean> = runCatching {
	log.debug("Connection.suspendVm ... ")
	this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	this.srvVm(vmId).suspend().send()

	this@suspendVm.expectVmStatus(vmId, VmStatus.SUSPENDED)
}.onSuccess {
	Term.VM.logSuccess("일시정지", vmId)
}.onFailure {
	Term.VM.logFail("일시정지", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.shutdownVm(vmId: String): Result<Boolean> = runCatching {
	log.debug("Connection.shutdownVm ... ")
	/*val vm: Vm = */this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	this.srvVm(vmId).shutdown().send()
	// TODO: 종료되지 않고 다시 올라올때가 잇음, expectVmStatus대신 다른 함수 써야 할지 확인 필요
//	if (!this@shutdownVm.expectVmStatus(vmId, VmStatus.DOWN)) {
//		return Result.failure(Error("종료 시간 초과"))
//	}
//	return Result.success(true)
	this@shutdownVm.expectVmStatus(vmId, VmStatus.DOWN)
}.onSuccess {
	Term.VM.logSuccess("종료", vmId)
}.onFailure {
	Term.VM.logFail("종료", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.rebootVm(vmId: String): Result<Boolean> = runCatching {
	log.debug("Connection.rebootVm ... ")
	val vm: Vm = this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	this.srvVm(vmId).reboot().send()
//	if (!this@rebootVm.expectVmStatus(vmId, VmStatus.UP)) {
//		log.error("가상머신 재부팅 시간 초과: {}", vm.name())
//		return Result.failure(Error("가상머신 재부팅 시간 초과"))
//	}
//	return Result.success(true)
	this@rebootVm.expectVmStatus(vmId, VmStatus.UP)
}.onSuccess {
	Term.VM.logSuccess("재부팅", vmId)
}.onFailure {
	Term.VM.logFail("재부팅", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.resetVm(vmId: String): Result<Boolean> = runCatching {
	val vm: Vm = this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	this.srvVm(vmId).reset().send()
//	if (!this@resetVm.expectVmStatus(vmId, VmStatus.UP)) {
//		log.error("가상머신 재설정 시간 초과: {}", vm.name())
//		return Result.failure(Error("가상머신 재설정 시간 초과"))
//	}
//	return Result.success(true)
	this@resetVm.expectVmStatus(vmId, VmStatus.UP)
}.onSuccess {
	Term.VM.logSuccess("재설정", vmId)
}.onFailure {
	Term.VM.logFail("재설정", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}


fun Connection.addDisksFromVm(vmId: String, diskIds: List<String> = listOf(), disks: List<Disk> = listOf()): Result<Boolean> = runCatching {
	log.debug("Connection.addDisksFromVm ... ")
	val vm: Vm =
		this.findVm(vmId)
			.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()

	// disk 생성되는 방법이 두가지
	// 생성과 연결, 생성의 경우 id가 없음/연결의 경우 id가 있음
	// 먼저 해야하는건 (생성) disk 객체에 든


	val disk: List<Disk> =
		diskIds.mapNotNull { this.findDisk(it).getOrNull() } // 왜 또 조회하는지?
	val diskAttachments: MutableList<DiskAttachment> = mutableListOf()

	// 생성
	for (disked in disks) {
		val d: Result<Disk?> = this.addDisk(disked)
		d.getOrNull()?.let { diskAttachments.add(DiskAttachmentBuilder().id(disked.id()).build()) }
	}

	val res: List<DiskAttachment> =
		this@addDisksFromVm.addMultipleDiskAttachmentsToVm(vm.id(), diskAttachments)
			.getOrDefault(listOf())
//	val finalRes: Boolean = res.all { it.isSuccess }
//
//	finalRes
	true
}.onSuccess {
	Term.VM.logSuccessWithin(Term.DISK, "생성", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.DISK,"생성", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.addDiskAttachmentToVm(vmId: String, diskAttachment: DiskAttachment): Result<DiskAttachment> = runCatching {
	val diskAttachAdded: DiskAttachment =
		this.srvAllDiskAttachmentsFromVm(vmId).add().attachment(diskAttachment).send().attachment()
	log.info("디스크 붙이기 성공")
	diskAttachAdded
}.onSuccess {
	Term.VM.logSuccessWithin(Term.DISK, "붙이기", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.DISK,"붙이기", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.addMultipleDiskAttachmentsToVm(vmId: String, diskAttachments: List<DiskAttachment>): Result<List<DiskAttachment>> = runCatching {
	val attachedDisks = diskAttachments.map { diskAttachment ->
		this.addDiskAttachmentToVm(vmId, diskAttachment).getOrThrow()
	}
	log.info("여러 디스크 붙이기 성공")
	attachedDisks
}.onSuccess {
	Term.VM.logSuccessWithin(Term.DISK, "여러 개 붙이기", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.DISK, "여러 개 붙이기", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.addDiskAttachmentsToVm(vmId: String, disks: List<Disk>): Result<Boolean> = runCatching {
	val diskAttach: List<DiskAttachmentBuilder> = disks.map {
		DiskAttachmentBuilder().id(it.id())
	}

	for(attach in diskAttach) { this.addDiskAttachmentToVm(vmId, attach.build())	}
	log.info("가상머신 디스크 붙이기 성공")
	true
}.onFailure {
	log.error("vm 디스크 붙이기 실패 ... 이유: {}", it.localizedMessage)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.bootableDiskExist(vmId: String): Boolean =
	this.findAllDiskAttachmentsFromVm(vmId)
		.getOrDefault(listOf())
		.any { it.bootable() }

fun Connection.addVm(
	vm: Vm,
	vnicProfileIds: List<String> = listOf(),
	diskIds: List<String> = listOf(),
	disks: List<Disk> = listOf(),
	bootId: String = ""
): Result<Vm?> = runCatching {
	if (this.findAllVms()
			.getOrDefault(listOf())
			.nameDuplicateVm(vm.name())) {
		return FailureType.DUPLICATE.toResult(Term.VM.desc)
	}
	val vmAdded: Vm =
		this.srvVms().add().vm(vm).send().vm()

	// vnic 생성
	if (vnicProfileIds.isNotEmpty()) {
			this.addNicsFromVm(vmAdded.id(), vnicProfileIds)
	}
	// disk 연결, 생성
	// disk id -> diskattachment.add.disk(conn.finddisk(diskId))
	// disk -> diskBuilder().build() -> diskattachment.add.disk(disk)
	if (diskIds.isNotEmpty() || disks.isNotEmpty()){
		this.addDisksFromVm(vmAdded.id(), diskIds, disks)
	}

	// cdroms
	if (bootId.isNotEmpty()) {
		this.addCdromFromVm(vm.id(), bootId)
	}

	if (!this.expectVmStatus(vm.id(), VmStatus.DOWN)) {
		log.error("가상머신 생성 시간 초과: {}", vm.name())
		return Result.failure(Error("가상머신 생성 시간 초과"))
	}
	vmAdded
}.onSuccess {
	Term.VM.logSuccess("생성", it.id())
}.onFailure {
	Term.VM.logFail("생성", it)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.updateVm(vm: Vm): Result<Vm?> = runCatching {
	if (this.findAllVms()
			.getOrDefault(listOf())
			.nameDuplicateVm(vm.name(), vm.id())) {
		return FailureType.DUPLICATE.toResult(Term.VM.desc)
	}
	val vmUpdated: Vm =
		this.srvVm(vm.id()).update().vm(vm).send().vm()
	vmUpdated
}.onSuccess {
	Term.VM.logSuccess("편집", it.id())
}.onFailure {
	Term.VM.logFail("편집", it)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.removeVm(vmId: String, detachOnly: Boolean = false): Result<Boolean> = runCatching {
	val vm: Vm =
		this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	if (vm.deleteProtected())
		throw ErrorPattern.VM_PROTECTED.toError()

	this.srvVm(vmId).remove().detachOnly(!detachOnly).send()
	this.expectVmDeleted(vmId)
}.onSuccess {
	Term.VM.logSuccess("삭제", vmId)
}.onFailure {
	Term.VM.logFail("삭제", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

@Throws(InterruptedException::class)
fun Connection.expectVmDeleted(vmId: String, timeout: Long = 60000L, interval: Long = 1000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val vms: List<Vm> =
			this.findAllVms().getOrDefault(listOf())
		val vmToRemove = vms.firstOrNull() { it.id() == vmId } // vm이 어느것이라도 매치되는것이 있다면
		if (vmToRemove == null) {// !(매치되는것이 있다)
			Term.VM.logSuccess("삭제")
			return true
		} else if (System.currentTimeMillis() - startTime > timeout){
			log.error("{} {} 삭제 시간 초과", Term.VM.desc, vmToRemove.name())
			return false
		}
		log.debug("{} 삭제 진행중 ... ", Term.VM.desc)
		Thread.sleep(interval)
	}
}

fun Connection.exportVm(vmId: String,
						hostName: String,
						directory: String,
						fileName: String): Result<Boolean> = runCatching {
	this@exportVm.srvVm(vmId).exportToPathOnHost()
		.host(HostBuilder().name(hostName))
		.directory(directory)
		.filename("$fileName.ova")
		.send()
	true
}.onSuccess {
	Term.VM.logSuccess("내보내기", vmId)
}.onFailure {
	Term.VM.logFail("내보내기", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.migrationVm(vmId: String, hostId: String): Result<Boolean> = runCatching {
	this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	val host: Host = this.findHost(hostId).getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
	this.srvVm(vmId).migrate().host(host).send()

//	val startTime = System.currentTimeMillis()
	val updatedVm: Vm = this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	return@runCatching updatedVm.host().id() == host.id()
}.onSuccess {
	Term.VM.logSuccess("마이그레이션", vmId)
}.onFailure {
	Term.VM.logFail("마이그레이션", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.cancelMigrationVm(vmId: String): Result<Boolean> = runCatching {
	this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	this.srvVm(vmId).cancelMigration().send()
	true
}.onSuccess {
	Term.VM.logSuccess("마이그레이션 취소", vmId)
}.onFailure {
	Term.VM.logFail("마이그레이션 취소", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}


private fun Connection.srvVmCdromsFromVm(vmId: String): VmCdromsService =
	this.srvVm(vmId).cdromsService()

fun Connection.findAllVmCdromsFromVm(vmId: String): Result<List<Cdrom>> = runCatching {
	this.srvVmCdromsFromVm(vmId).list().send().cdroms()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.CD_ROM, "목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.CD_ROM, "목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

private fun Connection.srvVmCdromFromVm(vmId: String, cdromId: String): VmCdromService =
	this.srvVmCdromsFromVm(vmId).cdromService(cdromId)

fun Connection.findVmCdromFromVm(vmId: String, cdromId: String): Result<Cdrom?> = runCatching {
	this.srvVmCdromFromVm(vmId, cdromId).get().send().cdrom()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.CD_ROM, "상세조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.CD_ROM, "상세조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.addCdromFromVm(vmId: String, bootId: String): Result<Cdrom> = runCatching {
	this.srvVmCdromsFromVm(vmId).add().cdrom(
		CdromBuilder().file(FileBuilder().id(bootId))
	).send().cdrom()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.CD_ROM, "생성", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.CD_ROM, "생성", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.updateCdromFromVm(vmId: String, cdromId: String, cdrom: Cdrom): Result<Cdrom?> = runCatching {
	this.srvVmCdromFromVm(vmId, cdromId).update().cdrom(cdrom).current(true).send().cdrom()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.CD_ROM, "편집", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.CD_ROM, "편집", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

private fun Connection.srvNicsFromVm(vmId: String): VmNicsService =
	this.srvVm(vmId).nicsService()

fun Connection.findAllNicsFromVm(vmId: String, follow: String = ""): Result<List<Nic>> = runCatching {
	if (follow.isNotEmpty())
		this.srvNicsFromVm(vmId).list().follow(follow).send().nics()
	else
		this.srvNicsFromVm(vmId).list().send().nics()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}



fun Connection.srvNicFromVm(vmId: String, nicId: String): VmNicService =
	this.srvNicsFromVm(vmId).nicService(nicId)

fun Connection.findNicFromVm(vmId: String, nicId: String): Result<Nic?> = runCatching {
	this.srvNicFromVm(vmId, nicId).get().send().nic()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.addNicFromVm(vmId: String, nic: Nic): Result<Nic?> = runCatching {
	val existingNics = this.findAllNicsFromVm(vmId).getOrDefault(listOf())
	if (existingNics.nameDuplicateVmNic(nic.name())) {
		return FailureType.DUPLICATE.toResult(Term.NIC.desc)
	}

	if (nic.macPresent() && nic.mac().addressPresent() && nic.mac().address().isNotEmpty()) {
		if (existingNics.any { it.mac().address() == nic.mac().address() })
			return FailureType.DUPLICATE.toResult("mac 주소")
	}
	srvNicsFromVm(vmId).add().nic(nic).send().nic()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "생성", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "생성", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.addNicsFromVm(vmId: String, vnicProfileIds: List<String>): Result<Boolean> = runCatching {
	val nics = vnicProfileIds.mapIndexed { index, profileId ->
		NicBuilder().name("nic${index + 1}")
			.vnicProfile(VnicProfileBuilder().id(profileId).build())
			.build()
	}

	val results = nics.map { addNicFromVm(vmId, it) }
	val allSuccessful = results.all { it.isSuccess }

	if (!allSuccessful) {
		val failedResults = results.filter { it.isFailure }
		log.warn("일부 NIC 생성 실패: ${failedResults.size}개")
	}
	allSuccessful
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "생성 여러개", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "생성 여러개", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.updateNicFromVm(vmId: String, nic: Nic): Result<Nic?> = runCatching {
	if (this.findAllNicsFromVm(vmId)
			.getOrDefault(listOf())
			.nameDuplicateVmNic(nic.name(), nic.id())) {
		return FailureType.DUPLICATE.toResult(Term.NIC.desc)
	}
	srvNicFromVm(vmId, nic.id()).update().nic(nic).send().nic()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "편집", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "편집", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.removeNicFromVm(vmId: String, nicId: String): Result<Boolean> = runCatching {
	val nic: Nic = this@removeNicFromVm.findNicFromVm(vmId, nicId)
		.getOrNull() ?: throw ErrorPattern.NIC_NOT_FOUND.toError()

	if (nic.plugged()) {
		log.error("nic가 plug된 상태")
		return Result.failure(Error("nic가 plug된 상태"))
	}
	srvNicFromVm(vmId, nicId).remove().send()
	true
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "제거", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "제거", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun List<Nic>.nameDuplicateVmNic(name: String, id: String? = null): Boolean =
	this.filter { it.id() != id }.any { it.name() == name }


private fun Connection.srvAllDiskAttachmentsFromVm(vmId: String): DiskAttachmentsService =
	this.srvVm(vmId).diskAttachmentsService()

fun Connection.findAllDiskAttachmentsFromVm(vmId: String): Result<List<DiskAttachment>> = runCatching {
	this.srvAllDiskAttachmentsFromVm(vmId).list().send().attachments()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.DISK_ATTACHMENT, "목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.DISK_ATTACHMENT, "목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.srvDiskAttachmentFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentService =
	this.srvVm(vmId).diskAttachmentsService().attachmentService(diskAttachmentId)

fun Connection.findDiskAttachmentFromVm(vmId: String, diskAttachmentId: String): Result<DiskAttachment?> = runCatching {
	this.srvDiskAttachmentFromVm(vmId, diskAttachmentId).get().send().attachment()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.DISK_ATTACHMENT, "상세조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.DISK_ATTACHMENT, "상세조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.findAllVmsFromDisk(diskId: String): Result<List<Vm>> = runCatching {
	val vms: List<Vm> =
		this.findAllVms(follow = "diskattachments")
			.getOrDefault(listOf())
			.filter { vm ->
				vm.diskAttachments().any { diskAttachment ->
					diskAttachment.disk().id() == diskId
				}
			}
	vms
}.onSuccess {
	Term.VM.logSuccessWithin(Term.VM, "목록조회", diskId)
}.onFailure {
	Term.VM.logFailWithin(Term.VM, "목록조회", it, diskId)
	throw if (it is Error) it.toItCloudException() else it
}



private fun Connection.srvSnapshotsFromVm(vmId: String): SnapshotsService =
	this.srvVm(vmId).snapshotsService()

fun Connection.findAllSnapshotsFromVm(vmId: String): Result<List<Snapshot>> = runCatching {
	/*val vm: Vm = */this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	srvSnapshotsFromVm(vmId).list().send().snapshots()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

private fun Connection.srvSnapshotFromVm(vmId: String, snapshotId: String): SnapshotService =
	this.srvSnapshotsFromVm(vmId).snapshotService(snapshotId)

fun Connection.findSnapshotFromVm(vmId: String, snapshotId: String): Result<Snapshot?> = runCatching {
	/*val vm: Vm = */this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	this.srvSnapshotFromVm(vmId, snapshotId).get().send().snapshot()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "상세조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "상세조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.addSnapshotFromVm(vmId: String, snapshot: Snapshot): Result<Snapshot> = runCatching {
	/*val vm: Vm = */this.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
	val snapshotAdded: Snapshot = this.srvSnapshotsFromVm(vmId).add().snapshot(snapshot).send().snapshot()
	this@addSnapshotFromVm.expectSnapshotStatusFromVm(vmId, snapshotAdded.id(), SnapshotStatus.OK)
	snapshotAdded
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "생성", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "생성", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.removeSnapshotFromVm(vmId: String, snapshotId: String): Result<Boolean> = runCatching {
	this.srvSnapshotFromVm(vmId, snapshotId).remove().send()
	if (!this@removeSnapshotFromVm.isSnapshotDeletedFromVm(vmId, snapshotId))
		return Result.failure(Error("스냅샷 생성 시간 초과"))
	true
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "삭제", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "삭제", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.undoSnapshotFromVm(vmId: String): Result<Boolean> = runCatching {
	this.srvVm(vmId).undoSnapshot().send()
	true
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "되돌리기", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "되돌리기", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.commitSnapshotFromVm(vmId: String): Result<Boolean> = runCatching {
	this.srvVm(vmId).commitSnapshot().send()
	true
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "커밋", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "커밋", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.previewSnapshotFromVm(vmId: String, snapshot: Snapshot, restoreMemory: Boolean): Result<Boolean> = runCatching {
	this.srvVm(vmId).previewSnapshot().restoreMemory(restoreMemory).snapshot(snapshot).send()
	true
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "미리보기", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "미리보기", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvSnapshotDisksFromVm(vmId: String, snapshotId: String): SnapshotDisksService =
	this.srvSnapshotFromVm(vmId, snapshotId).disksService()

fun Connection.findAllSnapshotDisksFromVm(vmId: String, snapshotId: String): Result<List<Disk>> = runCatching {
	this.srvSnapshotDisksFromVm(vmId, snapshotId).list().send().disks()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "디스크 목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "디스크 목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

private fun Connection.srvSnapshotNicsFromVm(vmId: String, snapshotId: String): SnapshotNicsService =
	this.srvSnapshotFromVm(vmId, snapshotId).nicsService()

fun Connection.findAllSnapshotNicsFromVm(vmId: String, snapshotId: String): Result<List<Nic>> = runCatching {
	this.srvSnapshotNicsFromVm(vmId, snapshotId).list().send().nics()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.SNAPSHOT, "NIC 목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.SNAPSHOT, "NIC 목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvVmGraphicsConsolesFromVm(vmId: String): VmGraphicsConsolesService =
	this.srvVm(vmId).graphicsConsolesService()

fun Connection.findAllVmGraphicsConsolesFromVm(vmId: String): List<GraphicsConsole> =
	this.srvVmGraphicsConsolesFromVm(vmId).list().current(true).send().consoles()

private fun Connection.srvVmGraphicsConsoleFromVm(vmId: String, graphicsConsoleId: String): VmGraphicsConsoleService =
	this.srvVmGraphicsConsolesFromVm(vmId).consoleService(graphicsConsoleId)

fun Connection.findTicketFromVmGraphicsConsole(vmId: String, graphicsConsoleId: String): Result<Ticket?> = runCatching {
	this.srvVmGraphicsConsoleFromVm(vmId, graphicsConsoleId).ticket().send().ticket()
}.onSuccess {
	Term.CONSOLE.logSuccessWithin(Term.TICKET, "상세조회", vmId)
}.onFailure {
	Term.CONSOLE.logFailWithin(Term.TICKET, "상세조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvStatisticsFromVm(vmId: String): StatisticsService =
	this.srvVm(vmId).statisticsService()

fun Connection.findAllStatisticsFromVm(vmId: String): List<Statistic> =
	this.srvStatisticsFromVm(vmId).list().send().statistics()

private fun Connection.srvAllAssignedPermissionsFromVm(vmId: String): AssignedPermissionsService =
	this.srvVm(vmId).permissionsService()

fun Connection.findAllAssignedPermissionsFromVm(vmId: String): Result<List<Permission>> = runCatching {
	this.srvAllAssignedPermissionsFromVm(vmId).list().send().permissions()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.PERMISSION, "목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.PERMISSION, "목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

private fun Connection.srvStatisticsFromVmNic(vmId: String, nicId: String): StatisticsService =
	this.srvVm(vmId).nicsService().nicService(nicId).statisticsService()

fun Connection.findAllStatisticsFromVmNic(vmId: String, nicId: String): Result<List<Statistic>> = runCatching {
	this.srvStatisticsFromVmNic(vmId, nicId).list().send().statistics()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "통계 목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "통계 목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

private fun Connection.srvAffinityLabelsFromVm(vmId: String): AssignedAffinityLabelsService =
	this.srvVm(vmId).affinityLabelsService()

fun Connection.findAllAffinityLabelsFromVm(vmId: String): Result<List<AffinityLabel>> = runCatching {
	this.srvAffinityLabelsFromVm(vmId).list().send().label()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.AFFINITY_LABEL, "목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.AFFINITY_LABEL, "목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.srvNicNetworkFilterParametersFromVm(vmId: String, nicId: String): NicNetworkFilterParametersService =
	this.srvNicFromVm(vmId, nicId).networkFilterParametersService()

fun Connection.findAllNicNetworkFilterParametersFromVm(vmId: String, nicId: String): Result<List<NetworkFilterParameter>> = runCatching {
	this.srvNicNetworkFilterParametersFromVm(vmId, nicId).list().send().parameters()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "NFP 목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "NFP 목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.addNicNetworkFilterParameterFromVm(vmId: String, nicId: String, nfp: NetworkFilterParameter): Result<NetworkFilterParameter> = runCatching {
	this.srvNicNetworkFilterParametersFromVm(vmId, nicId).add().parameter(nfp).send().parameter()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "NFP 생성", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "NFP 생성", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.srvReportedDevicesFromVm(vmId: String): VmReportedDevicesService =
	this.srvVm(vmId).reportedDevicesService()

fun Connection.findAllReportedDevicesFromVm(vmId: String): Result<List<ReportedDevice>> = runCatching {
	this@findAllReportedDevicesFromVm.srvReportedDevicesFromVm(vmId).list().send().reportedDevice()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "NFP 생성", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "NFP 생성", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun Connection.srvReportedDevicesFromVmNics(vmId: String, nicId: String): VmReportedDevicesService =
	this.srvNicFromVm(vmId, nicId).reportedDevicesService()

fun Connection.findAllReportedDeviceFromVmNic(vmId: String, nicId: String, follow: String = ""): Result<List<ReportedDevice>> = runCatching {
	if (follow.isNotEmpty())
		this.srvReportedDevicesFromVmNics(vmId, nicId).list().follow(follow).send().reportedDevice()
	else
		this.srvReportedDevicesFromVmNics(vmId, nicId).list().send().reportedDevice()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.NIC, "보고된 디바이스 목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.NIC, "보고된 디바이스 목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

private fun Connection.srvApplicationsFromVm(vmId: String): VmApplicationsService =
	this.srvVm(vmId).applicationsService()

fun Connection.findAllApplicationsFromVm(vmId: String, follow: String = ""): Result<List<Application>> = runCatching {
	if (follow.isNotEmpty())
		this.srvApplicationsFromVm(vmId).list().follow(follow).send().applications()
	else
		this.srvApplicationsFromVm(vmId).list().send().applications()
}.onSuccess {
	Term.VM.logSuccessWithin(Term.APPLICATION, "목록조회", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.APPLICATION, "목록조회", it, vmId)
	throw if (it is Error) it.toItCloudException() else it

}

fun List<Vm>.nameDuplicateVm(name: String, id: String? = null): Boolean =
	this.filter { it.id() != id }.any { it.name() == name }



/**
 * 가상머신 상태확인
 *
 * @param vmId 가상머신 아이디
 * @param expectStatus 변경되야하는 가상머신 상태
 * @param interval 대기 초
 * @param timeout 총 대기시간
 * @return 200/404
 * @throws InterruptedException
 */
@Throws(InterruptedException::class)
fun Connection.expectVmStatus(vmId: String, expectStatus: VmStatus, interval: Long = 1000L, timeout: Long = 90000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val currentVm: Vm? = this.findVm(vmId).getOrNull()
		val status = currentVm?.status()
		if (status == expectStatus) {
			log.info("vm {} 완료...", expectStatus)
			return true
		} else if (System.currentTimeMillis() - startTime > timeout) {
			log.error("vm {} 전환 시간 초과", expectStatus)
			return false
		}
		log.info("가상머신 상태: {}", status)
		Thread.sleep(interval)
	}
}


/**
 * [Connection.expectSnapshotStatusFromVm]
 * 스냅샷 상태
 *
 * @param expectStatus
 * @param interval
 * @param timeout
 * @return
 * @throws InterruptedException
 */
@Throws(InterruptedException::class)
fun Connection.expectSnapshotStatusFromVm(vmId: String, snapshotId: String, expectStatus: SnapshotStatus = SnapshotStatus.OK, timeout: Long = 90000L, interval: Long = 1000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val snapshot: Snapshot? = this@expectSnapshotStatusFromVm.findSnapshotFromVm(vmId, snapshotId).getOrNull()
		val status: SnapshotStatus = snapshot?.snapshotStatus() ?: SnapshotStatus.LOCKED

		if (status == expectStatus)
			return true
		else if (System.currentTimeMillis() - startTime > timeout)
			return false

		log.info("스냅샷 상태: {}", status)
		Thread.sleep(interval)
	}
}

/**
 * [Connection.isSnapshotDeletedFromVm]
 * 스냅샷 삭제 확인
 *
 * @param snapshotId
 * @param interval
 * @param timeout
 * @return
 * @throws InterruptedException
 */
@Throws(InterruptedException::class)
fun Connection.isSnapshotDeletedFromVm(vmId: String, snapshotId: String, timeout: Long = 60000L, interval: Long = 1000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val snapshots: List<Snapshot> = this@isSnapshotDeletedFromVm.findAllSnapshotsFromVm(vmId).getOrDefault(listOf())
		val snapshotExists: Boolean = snapshots.any { it.id() == snapshotId }
		if (!snapshotExists) {
			log.info("스냅샷이 존재하지 않음 = 삭제됨")
			return true
		} else if (System.currentTimeMillis() - startTime > timeout) {
			val desc: String = this@isSnapshotDeletedFromVm.findSnapshotFromVm(vmId, snapshotId).getOrNull()?.description() ?: ""
			log.error("스냅샷 삭제중: {}", desc)
			return false // 타임아웃
		}
		log.info("스냅샷 삭제 진행 중: $snapshotId")
		Thread.sleep(interval)
	}
}