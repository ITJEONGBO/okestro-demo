package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.Os
import com.itinfo.util.ovirt.findBios
import com.itinfo.util.ovirt.findVmType
import com.itinfo.itcloud.model.gson
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.ovirtDf
import com.itinfo.util.ovirt.findCluster
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.BiosType
import org.ovirt.engine.sdk4.types.Cluster
import org.ovirt.engine.sdk4.types.DataCenter
import org.ovirt.engine.sdk4.types.Template
import org.ovirt.engine.sdk4.types.VmType
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger

private val log = LoggerFactory.getLogger(TemplateVo::class.java)

/**
 * [TemplateVo]
 *
 * add example:
 * <template>
 *   <name>mytemplate</name>
 *   <vm id="123"/>
 * </template>
 * 
 * @property id [String] 템플릿 id
 * @property name [String] 템플릿 name
 * @property description [String]
 * @property comment [String]
 * @property versionName [String]
 * @property versionNum [Int]
 * @property createDate [String]
 * @property status [String]
 *  
 * 보관
 * @property osType [String]
 * @property chipsetFirmwareType [String] bios.type
 * @property optimizeOption [String] 최적화 옵션
 *
 * @property memory [BigInteger] 설정된 메모리
 * @property cpuCnt [Int] cpu 코어 수
 * @property cpuCoreCnt [Int]
 * @property cpuSocketCnt [Int]
 * @property cpuThreadCnt [Int]
 *
 * @property hostCluster [String]
 * @property monitor [Int] 모니터 수
 * @property ha [Boolean] 고가용성
 * @property priority [Int] 우선순위
 * @property usb [Boolean] usb
 * @property noneStocked [Boolean] 상태 비저장
 * @property origin [String] 소스
 *
 * @property List<[DiskImageVo]> diskVos 디스크 할당
 *
 * @property clusterId [String]
 * @property clusterName [String]
 * @property datacenterId [String]
 * @property datacenterName [String]
 **/
class TemplateVo(
	val id: String = "",
	val name: String = "",
	val description: String = "",
	val versionName: String = "",
	val versionNum: Int = 0,
	val createDate: String = "",
	val status: String = "",
	val osType: String = "",
	val chipsetFirmwareType: String = "",
	val optimizeOption: String = "",
	val memory: BigInteger = BigInteger.ZERO,
	val cpuCnt: Int = 0,
	val cpuCoreCnt: Int = 0,
	val cpuSocketCnt: Int = 0,
	val cpuThreadCnt: Int = 0,
	val hostCluster: String = "",
	val monitor: Int = 0,
	val isHa: Boolean = false,
	val priority: Int = 0,
	val isUsb: Boolean = false,
	val isNoneStocked: Boolean = false,
	val origin: String = "",
	val diskVos: List<DiskImageVo> = listOf(),
	val vmVo: VmVo = VmVo(),
	val clusterVo: ClusterVo = ClusterVo(),
	val dataCenterVo: DataCenterVo = DataCenterVo(),
): Serializable {
	override fun toString(): String =
		gson.toJson(this)
		
	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bVersionName: String = "";fun versionName(block: () -> String?) { bVersionName = block() ?: "" }
		private var bVersionNum: Int = 0;fun versionNum(block: () -> Int?) { bVersionNum = block() ?: 0 }
		private var bCreateDate: String = "";fun createDate(block: () -> String?) { bCreateDate = block() ?: "" }
		private var bStatus: String = "";fun status(block: () -> String?) { bStatus = block() ?: "" }
		private var bOsType: String = "";fun osType(block: () -> String?) { bOsType = block() ?: "" }
		private var bChipsetFirmwareType: String = "";fun chipsetFirmwareType(block: () -> String?) { bChipsetFirmwareType = block() ?: "" }
		private var bOptimizeOption: String = "";fun optimizeOption(block: () -> String?) { bOptimizeOption = block() ?: "" }
		private var bMemory: BigInteger = BigInteger.ZERO;fun memory(block: () -> BigInteger?) { bMemory = block() ?: BigInteger.ZERO }
		private var bCpuCnt: Int = 0;fun cpuCnt(block: () -> Int?) { bCpuCnt = block() ?: 0 }
		private var bCpuCoreCnt: Int = 0;fun cpuCoreCnt(block: () -> Int?) { bCpuCoreCnt = block() ?: 0 }
		private var bCpuSocketCnt: Int = 0;fun cpuSocketCnt(block: () -> Int?) { bCpuSocketCnt = block() ?: 0 }
		private var bCpuThreadCnt: Int = 0;fun cpuThreadCnt(block: () -> Int?) { bCpuThreadCnt = block() ?: 0 }
		private var bHostCluster: String = "";fun hostCluster(block: () -> String?) { bHostCluster = block() ?: "" }
		private var bMonitor: Int = 0;fun monitor(block: () -> Int?) { bMonitor = block() ?: 0 }
		private var bIsHa: Boolean = false;fun isHa(block: () -> Boolean?) { bIsHa = block() ?: false }
		private var bPriority: Int = 0;fun priority(block: () -> Int?) { bPriority = block() ?: 0 }
		private var bIsUsb: Boolean = false;fun isUsb(block: () -> Boolean?) { bIsUsb = block() ?: false }
		private var bIsNoneStocked: Boolean = false;fun isNoneStocked(block: () -> Boolean?) { bIsNoneStocked = block() ?: false }
		private var bOrigin: String = "";fun origin(block: () -> String?) { bOrigin = block() ?: "" }
		private var bDiskVos: List<DiskImageVo> = listOf();fun diskVos(block: () -> List<DiskImageVo>?) { bDiskVos = block() ?: listOf() }
		private var bVmVo: VmVo = VmVo();fun vmVo(block: () -> VmVo?) { bVmVo = block() ?: VmVo() }
		private var bClusterVo: ClusterVo = ClusterVo();fun clusterVo(block: () -> ClusterVo?) { bClusterVo = block() ?: ClusterVo() }
		private var bDatacenterVo: DataCenterVo = DataCenterVo();fun datacenterVo(block: () -> DataCenterVo?) { bDatacenterVo = block() ?: DataCenterVo() }

		fun build(): TemplateVo = TemplateVo(bId, bName, bDescription, bVersionName, bVersionNum, bCreateDate, bStatus, bOsType, bChipsetFirmwareType, bOptimizeOption, bMemory, bCpuCnt, bCpuCoreCnt, bCpuSocketCnt, bCpuThreadCnt, bHostCluster, bMonitor, bIsHa, bPriority, bIsUsb, bIsNoneStocked, bOrigin, bDiskVos, bVmVo, bClusterVo, bDatacenterVo,)
	}

	companion object {
		inline fun builder(block: TemplateVo.Builder.() -> Unit): TemplateVo = TemplateVo.Builder().apply(block).build()
	}
}

fun Template.toTemplateVo(conn: Connection): TemplateVo {
	log.debug("Template.toTemplateVo ... ")
	val cluster: Cluster? =
		if(this@toTemplateVo.clusterPresent()) {
			conn.findCluster(this@toTemplateVo.cluster().id()).getOrNull()
		}else{
			null
		}
	val dataCenter: DataCenter? = cluster?.dataCenter()

	return TemplateVo.builder {
		id { this@toTemplateVo.id() }
		name { this@toTemplateVo.name() }
		description { this@toTemplateVo.description() }
		status { this@toTemplateVo.status().value() }
		versionName { if (this@toTemplateVo.versionPresent()) this@toTemplateVo.version().versionName() else "" }
		versionNum { if (this@toTemplateVo.versionPresent()) this@toTemplateVo.version().versionNumberAsInteger() else 0 }
		createDate { ovirtDf.format(this@toTemplateVo.creationTime().time) }
		osType { if (this@toTemplateVo.osPresent()) Os.findByCode(this@toTemplateVo.os().type()).fullName else null }
		chipsetFirmwareType { if (this@toTemplateVo.bios().typePresent()) this@toTemplateVo.bios().type().findBios() else null }
		optimizeOption { this@toTemplateVo.type().findVmType() } // 최적화 옵션
		memory { this@toTemplateVo.memoryPolicy().guaranteed() }
		cpuCoreCnt { this@toTemplateVo.cpu().topology().coresAsInteger() }
		cpuSocketCnt { this@toTemplateVo.cpu().topology().socketsAsInteger() }
		cpuThreadCnt { this@toTemplateVo.cpu().topology().threadsAsInteger() }
		cpuCnt {
			this@toTemplateVo.cpu().topology().coresAsInteger() *
			this@toTemplateVo.cpu().topology().socketsAsInteger() *
			this@toTemplateVo.cpu().topology().threadsAsInteger()
		}
		monitor { this@toTemplateVo.display().monitorsAsInteger() }
		isHa { this@toTemplateVo.highAvailability().enabled() }
		priority { this@toTemplateVo.highAvailability().priorityAsInteger() }
		isUsb { this@toTemplateVo.usb().enabled() }
		hostCluster { cluster?.name() }
		origin { this@toTemplateVo.origin() }
		clusterVo { cluster?.toClusterIdName() }
		datacenterVo { dataCenter?.toDataCenterIdName() }
	}
}

fun List<Template>.toTemplateVos(conn: Connection): List<TemplateVo> =
	this@toTemplateVos.map { it.toTemplateVo(conn) }


fun TemplateVo.toTemplateBuilder(): TemplateBuilder {
	return TemplateBuilder()
		.name(this@toTemplateBuilder.name)
		.vm(VmBuilder().id(this@toTemplateBuilder.vmVo.id))
}

fun TemplateVo.toTemplateBuilder4Add(): TemplateBuilder {
	return TemplateBuilder()
		.name(this@toTemplateBuilder4Add.name)
		.description(this@toTemplateBuilder4Add.description)
		.cluster(ClusterBuilder().id(this@toTemplateBuilder4Add.clusterVo.id))
}

fun TemplateVo.toTemplateBuilder4Edit(): TemplateBuilder {
	return TemplateBuilder()
		.id(this@toTemplateBuilder4Edit.id)
		.name(this@toTemplateBuilder4Edit.name)
		.description(this@toTemplateBuilder4Edit.description)
		.os(OperatingSystemBuilder().type(this@toTemplateBuilder4Edit.osType))
		.bios(BiosBuilder().type(BiosType.valueOf(this@toTemplateBuilder4Edit.chipsetFirmwareType)))
		.cluster(ClusterBuilder().id(this@toTemplateBuilder4Edit.clusterVo.id))
		.type(VmType.valueOf(this@toTemplateBuilder4Edit.optimizeOption))
}
