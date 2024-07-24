package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.FailureType
import com.itinfo.util.ovirt.error.toError
import com.itinfo.util.ovirt.error.toResult
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.*
import kotlin.Error

object DataCenterName {
	const val KO: String = "클러스터"  //??
}

fun Connection.srvDataCenters(): DataCentersService =
	this.systemService.dataCentersService()

fun Connection.findAllDataCenters(search: String = "", follow: String = ""): Result<List<DataCenter>> = runCatching {
	if (search.isNotEmpty() && follow.isNotEmpty())
		this.srvDataCenters().list().search(search).follow(follow).send().dataCenters()
	else if (search.isNotEmpty())
		this.srvDataCenters().list().search(search).send().dataCenters()
	else if (follow.isNotEmpty())
		this.srvDataCenters().list().follow(follow).send().dataCenters()
	else
		this.srvDataCenters().list().send().dataCenters()
}.onSuccess {
	Term.DATACENTER.logSuccess("목록조회")
}.onFailure {
	Term.DATACENTER.logFail("목록조회", it)
	throw it
}

fun Connection.srvDataCenter(dataCenterId: String): DataCenterService =
	this.srvDataCenters().dataCenterService(dataCenterId)

fun Connection.findDataCenter(dcId: String): Result<DataCenter?> = runCatching {
	this.srvDataCenter(dcId).get().send().dataCenter()
}.onSuccess {
	Term.DATACENTER.logSuccess("상세조회")
}.onFailure {
	Term.DATACENTER.logFail("상세조회", it)
	throw it
}

fun Connection.findDataCenterName(dataCenterId: String): String =
	this.srvDataCenter(dataCenterId).get().send().dataCenter().name()

fun List<DataCenter>.nameDuplicateDataCenter(dataCenterName: String, dataCenterId: String? = null): Boolean =
	this.filter { it.id() != dataCenterId }.any { it.name() == dataCenterName }

fun Connection.addDataCenter(dataCenter: DataCenter): Result<DataCenter?> = runCatching {
	if (this.findAllDataCenters()
			.getOrDefault(listOf())
			.nameDuplicateDataCenter(dataCenter.name())) {
		return FailureType.DUPLICATE.toResult(Term.DATACENTER.desc)
	}
	val dataCenterAdded: DataCenter =
		this.srvDataCenters().add().dataCenter(dataCenter).send().dataCenter()
	dataCenterAdded
}.onSuccess {
	Term.DATACENTER.logSuccess("생성")
}.onFailure {
	Term.DATACENTER.logFail("생성", it)
	throw it
}

fun Connection.updateDataCenter(dataCenter: DataCenter): Result<DataCenter?> = runCatching {
	if (this.findAllDataCenters()
			.getOrDefault(listOf())
			.nameDuplicateDataCenter(dataCenter.name(), dataCenter.id())) {
		return FailureType.DUPLICATE.toResult(Term.DATACENTER.desc)
	}
    val dataCenterUpdated: DataCenter =
		this.srvDataCenter(dataCenter.id()).update().dataCenter(dataCenter).send().dataCenter()
	dataCenterUpdated
}.onSuccess {
	Term.DATACENTER.logSuccess("편집")
}.onFailure {
	Term.DATACENTER.logFail("편집", it)
	throw it
}

fun Connection.removeDataCenter(dataCenterId: String): Result<Boolean> = runCatching {
	val dataCenter: DataCenter =
		this.findDataCenter(dataCenterId).getOrNull() ?: throw ErrorPattern.DATACENTER_NOT_FOUND.toError()

	this.srvDataCenter(dataCenter.id()).remove().force(true).send()
	this.expectDataCenterDeleted(dataCenterId)
	true
}.onSuccess {
	Term.DATACENTER.logSuccess("삭제")
}.onFailure {
	Term.DATACENTER.logFail("삭제", it)
	throw it
}

@Throws(InterruptedException::class)
fun Connection.expectDataCenterDeleted(dataCenterId: String, timeout: Long = 60000L, interval: Long = 1000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val dataCenters: List<DataCenter> =
			this.findAllDataCenters().getOrDefault(listOf())
		val dataCenterToRemove = dataCenters.firstOrNull() { it.id() == dataCenterId }
		if (dataCenterToRemove == null) { // dataCenterToRemove 에 아무것도 없으면(삭제된상태)
			Term.DATACENTER.logSuccess("삭제")
			return true
		}
		else if (System.currentTimeMillis() - startTime > timeout) {
			log.error("{} {} 삭제 실패 ... 시간 초과", Term.DATACENTER.desc, dataCenterToRemove.name())
			return false
		}
		log.debug("{} 삭제 진행중 ... ", Term.DATACENTER.desc)
		Thread.sleep(interval)
	}
}


fun Connection.srvClustersFromDataCenter(dataCenterId: String): ClustersService =
	this.srvDataCenter(dataCenterId).clustersService()

fun Connection.findAllClustersFromDataCenter(dataCenterId: String): Result<List<Cluster>> = runCatching {
	this.srvClustersFromDataCenter(dataCenterId).list().send().clusters()
}.onSuccess {
	Term.DATACENTER.logSuccessWithin(Term.CLUSTER,"목록조회", dataCenterId)
}.onFailure {
	Term.DATACENTER.logFailWithin(Term.CLUSTER,"목록조회", it, dataCenterId)
	throw it
}

fun Connection.srvNetworksFromFromDataCenter(dataCenterId: String): DataCenterNetworksService =
	this.srvDataCenter(dataCenterId).networksService()

fun Connection.findAllNetworksFromFromDataCenter(dataCenterId: String): Result<List<Network>> = runCatching {
	this.srvNetworksFromFromDataCenter(dataCenterId).list().send().networks() ?: listOf()
}.onSuccess {
	Term.DATACENTER.logSuccessWithin(Term.NETWORK,"목록조회", dataCenterId)
}.onFailure {
	Term.DATACENTER.logFailWithin(Term.NETWORK,"목록조회", it, dataCenterId)
	throw it
}

fun Connection.srvAllAttachedStorageDomainsFromDataCenter(dataCenterId: String): AttachedStorageDomainsService =
	this.srvDataCenter(dataCenterId).storageDomainsService()

fun Connection.findAllAttachedStorageDomainsFromDataCenter(dataCenterId: String): Result<List<StorageDomain>> = runCatching {
	this.findDataCenter(dataCenterId).getOrNull() ?: throw ErrorPattern.DATACENTER_NOT_FOUND.toError()
	this.srvAllAttachedStorageDomainsFromDataCenter(dataCenterId).list().send().storageDomains()
}.onSuccess {
	Term.DATACENTER.logSuccessWithin(Term.STORAGE_DOMAIN,"목록조회", dataCenterId)
}.onFailure {
	Term.DATACENTER.logFailWithin(Term.STORAGE_DOMAIN,"목록조회", it, dataCenterId)
	throw it
}

fun Connection.srvAttachedStorageDomainFromDataCenter(dataCenterId: String, storageDomainId: String): AttachedStorageDomainService =
	this.srvAllAttachedStorageDomainsFromDataCenter(dataCenterId).storageDomainService(storageDomainId)

fun Connection.findAllAttachedStorageDomainDisksFromDataCenter(dataCenterId: String, storageDomainId: String): Result<List<Disk>> = runCatching {
	this.srvAttachedStorageDomainFromDataCenter(dataCenterId, storageDomainId).disksService().list().send().disks()
}.onSuccess {
	Term.DATACENTER.logSuccessWithin(Term.DISK,"목록조회", dataCenterId)
}.onFailure {
	Term.DATACENTER.logFailWithin(Term.DISK,"목록조회", it, dataCenterId)
	throw it
}

fun Connection.findAttachedStorageDomainFromDataCenter(dataCenterId: String, storageDomainId: String): StorageDomain? = try {
	srvAttachedStorageDomainFromDataCenter(dataCenterId, storageDomainId).get().send().storageDomain()
} catch (e: Error) {
	log.error(e.localizedMessage)
	null
}

fun Connection.activeAttachedStorageDomainFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean = try {
	this.srvAttachedStorageDomainFromDataCenter(dataCenterId, storageDomainId).activate().send()
	true
} catch (e: Error) {
	log.error(e.localizedMessage)
	false
}

fun Connection.deactiveAttachedStorageDomainFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean = try {
	this.srvAttachedStorageDomainFromDataCenter(dataCenterId, storageDomainId).deactivate().force(true).send()
	true
} catch (e: Error) {
	log.error(e.localizedMessage)
	false
}

fun Connection.removeAttachedStorageDomainFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean = try {
	this.srvAttachedStorageDomainFromDataCenter(dataCenterId, storageDomainId).remove().async(true).send()
	true
} catch (e: Error) {
	log.error(e.localizedMessage)
	false
}

private fun Connection.srvQossFromDataCenter(dataCenterId: String): QossService =
	this.srvDataCenter(dataCenterId).qossService()

fun Connection.findAllQossFromDataCenter(dataCenterId: String): List<Qos> =
	this.srvQossFromDataCenter(dataCenterId).list().send().qoss()

private fun Connection.srvQuotasFromDataCenter(dataCenterId: String): QuotasService =
	this.srvDataCenter(dataCenterId).quotasService()

fun Connection.findAllQuotasFromDataCenter(dataCenterId: String): List<Quota> =
	this.srvQuotasFromDataCenter(dataCenterId).list().send().quotas()

fun Connection.addQuotaFromDataCenter(dataCenterId: String, quota: Quota): Quota? = try {
	this.srvQuotasFromDataCenter(dataCenterId).add().quota(quota).send().quota()
} catch (e: Error) {
	log.error(e.localizedMessage)
	null
}

private fun Connection.srvQuotaFromDataCenter(dataCenterId: String, quotaId: String): QuotaService =
	this.srvQuotasFromDataCenter(dataCenterId).quotaService(quotaId)

fun Connection.findQuotaFromDataCenter(dataCenterId: String, quotaId: String): Quota =
	this.srvQuotaFromDataCenter(dataCenterId, quotaId).get().send().quota()

private fun Connection.srvQuotaClusterLimitsFromDataCenter(dataCenterId: String, quotaId: String): QuotaClusterLimitsService =
	this.srvQuotaFromDataCenter(dataCenterId, quotaId).quotaClusterLimitsService()

fun Connection.findAllQuotaClusterLimitsFromDataCenter(dataCenterId: String, quotaId: String): List<QuotaClusterLimit> =
	this.srvQuotaClusterLimitsFromDataCenter(dataCenterId, quotaId).list().send().limits()

private fun Connection.srvQuotaStorageLimitsFromDataCenter(dataCenterId: String, quotaId: String): QuotaStorageLimitsService =
	this.srvQuotaFromDataCenter(dataCenterId, quotaId).quotaStorageLimitsService()

fun Connection.findAllQuotaStorageLimitsFromDataCenter(dataCenterId: String, quotaId: String): List<QuotaStorageLimit> =
	this.srvQuotaStorageLimitsFromDataCenter(dataCenterId, quotaId).list().send().limits()