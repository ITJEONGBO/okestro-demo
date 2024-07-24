package com.itinfo.itcloud

import com.itinfo.util.ovirt.findSnapshotFromVm
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("com.itinfo.itcloud.ExpectStatusExtKt")

/*
@Throws(InterruptedException::class)
fun List<Cluster>.isDeleted(clusterId: String, interval: Long = 1000L, timeout: Long = 90000L): Boolean {
	log.debug("clusterExists ... ")
	val startTime = System.currentTimeMillis()
	while (true) {
		val clusterExists = this@isDeleted.any { cluster: Cluster -> cluster.id() == clusterId } // cluster 어느것이라도 매치되는것이 있다면

		if (!clusterExists) // !(매치되는것이 있다)
			return true
		else if (System.currentTimeMillis() - startTime > timeout)
			return false

		log.debug("클러스터 삭제 진행중 ... ")
		Thread.sleep(interval)
	}
}

@Throws(InterruptedException::class)
fun List<Vm>.isDeleted(vmId: String, interval: Long = 1000L, timeout: Long = 90000L): Boolean {
	log.debug("vmExists ... ")
	val startTime = System.currentTimeMillis()
	while (true) {
		val vmExists = this@isDeleted.any { vm: Vm -> vm.id() == vmId } // vm이 어느것이라도 매치되는것이 있다면

		if (!vmExists) // !(매치되는것이 있다)
			return true
		else if (System.currentTimeMillis() - startTime > timeout)
			return false

		log.debug("가상머신 삭제 진행중 ... ")
		Thread.sleep(interval)
	}
}@Throws(InterruptedException::class)
fun Connection.expectDiskStatus(diskId: String, expectStatus: DiskStatus = DiskStatus.OK, timeout: Long = 90000L, interval: Long = 1000L): Boolean {
	val startTime: Long = System.currentTimeMillis()
	while (true) {
		val disk: Disk? = this.findDisk(diskId)
		val status = disk?.status()

		if (status == expectStatus)
			return true
		else if (System.currentTimeMillis() - startTime > timeout)
			return false

		log.info("디스크 상태: {}", status)
		Thread.sleep(interval)
	}
}

@Throws(InterruptedException::class)
fun Connection.expectSnapshotStatusFromVm(vmId: String, snapshotId: String, expectStatus: SnapshotStatus = SnapshotStatus.OK, interval: Long = 1000L, timeout: Long = 90000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val snapshot: Snapshot? = this@expectSnapshotStatusFromVm.findSnapshotFromVm(vmId, snapshotId)
		val status: SnapshotStatus = snapshot?.snapshotStatus() ?: SnapshotStatus.LOCKED

		if (status == expectStatus)
			return true
		else if (System.currentTimeMillis() - startTime > timeout)
			return false

		log.info("스냅샷 상태: {}", status)
		Thread.sleep(interval)
	}
}
*/

/**
 * [List<Snapshot>.isDeleted]
 * 스냅샷 삭제 확인
 *
 * @param snapId
 * @param interval
 * @param timeout
 * @return
 * @throws InterruptedException
 */
@Throws(InterruptedException::class)
fun List<Snapshot>.isDeleted(snapId: String, interval: Long = 1000L, timeout: Long = 90000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val snapshotExists = this@isDeleted.any { snapshot: Snapshot -> snapshot.id() == snapId }

		if (!snapshotExists)
			return true // 스냅샷이 존재하지 않음 = 삭제됨
		else if (System.currentTimeMillis() - startTime > timeout)
			return false // 타임아웃

		log.info("스냅샷 삭제 진행 중: $snapId")
		Thread.sleep(interval)
	}
}