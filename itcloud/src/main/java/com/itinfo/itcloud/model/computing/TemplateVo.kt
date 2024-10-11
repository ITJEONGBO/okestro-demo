package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.Os
import com.itinfo.util.ovirt.findBios
import com.itinfo.util.ovirt.findVmType
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.fromClusterToIdentifiedVo
import com.itinfo.itcloud.model.fromDataCenterToIdentifiedVo
import com.itinfo.itcloud.model.network.NicVo
import com.itinfo.itcloud.model.network.VnicProfileVo
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import com.itinfo.util.ovirt.findCluster
import com.itinfo.util.ovirt.findDataCenter
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger
import java.util.*

private val log = LoggerFactory.getLogger(TemplateVo::class.java)

/**
 * [TemplateVo]
 *
 * @property id [String] template Id
 * @property name [String]
 * @property status [TemplateStatus] TemplateStatus
 * @property upTime [String]
 * @property creationTime [Date]
 *
 * <statistic>
 * @property memoryInstalled [BigInteger]
 * @property memoryUsed [BigInteger]
 * @property memoryBuffered [BigInteger]
 * @property memoryCached [BigInteger]
 * @property memoryFree [BigInteger]
 * @property memoryUnused [BigInteger]
 *
 * @property fqdn [String]
 * @property ipv4 [String]
 * @property ipv6 [String]
 * @property hostEngineVm [Boolean] 금장/은장 vm().origin() == "managed_hosted_engine"
 * @property placement [String] 호스트에 부착 여부 ( 호스트에 고정, 호스트에서 실행중, 호스트에서 고정 및 실행)
 * @property hostVo [HostVo]  실행 호스트 정보 (현재 실행되고 있는 호스트의 정보)
 * @property snapshotVos List<[IdentifiedVo]>
 * @property nicVos List<[NicVo]>
 * @property vmVo [IdentifiedVo]
 *
 * <일반>
 * @property dataCenterVo [IdentifiedVo]	따지고보면 생성창에서 보여주는 역할만 하는거 같음
 * @property clusterVo [IdentifiedVo]
 * @property versionName [String]
 * @property versionNum [Int]
 * @property baseTemplate [IdentifiedVo]
 *
 * @property description [String] 설명
 * @property comment [String]
 * @property osSystem [String]  // TODO model-OS
 * @property chipsetFirmwareType [String] bios.type
 * @property optimizeOption [String]        최적화 옵션
 * @property stateless [Boolean]			상태 비저장
 * @property startPaused [Boolean]			일시정지 모드에서 시작
 * @property deleteProtected [Boolean]  	삭제 방지
 * @property diskAttachmentVos List<[DiskAttachmentVo]>	인스턴스 이미지 (디스크 연결+생성)  // 전체 출력용 같이
 * @property vnicProfileVos List<[VnicProfileVo]>	vnic 프로파일 (vnic Profile id를 받아서
 *
 * <시스템, System>
 * @property memorySize [BigInteger] 		메모리 크기
 * @property memoryMax [BigInteger] 		최대 메모리
 * @property memoryActual [BigInteger]	할당할 실제 메모리
 * @property cpuArc [Architecture] cpu().architecture()
 * @property cpuTopologyCnt [Int]  총 가상 CPU  (게스트 cpu 수, CPU 코어수 (2:1:1) 토폴로지에서 모아서 하기)
 * @property cpuTopologyCore [Int]  가상 소켓
 * @property cpuTopologySocket [Int]  가상 소켓 당 코어
 * @property cpuTopologyThread [Int]  코어당 스레드
 * @property instanceType [String] 	인스턴스 유형 (none, large, medium, small, tiny, xlarge)
 * @property timeOffset [String] 	하드웨어 클럭의 시간 오프셋 기본값으로 하면됨 greenwich standard time, kst

 * <초기실행, Init>
 * @property cloudInit [Boolean]
 * @property hostName [String] ???
 * @property timeStandard [String]   // ??
 * 인증
 * 네트워크
 * @property script [String] 사용자 지정 스크립트
 *
 * <콘솔>
 * 기본설정 -> 그래픽 프로토콜(vnc)
 * @property monitor [Int]
 * @property usb [Boolean]
 *
 * <호스트>
 * @property hostInCluster [Boolean] 클러스터 내의 호스트 true-클러스터 내 호스트, false-특정
 * @property hostVos List<[HostVo]> 클러스터 내의 호스트 false -> 특정 호스트 선택 (호스트는 여러개 존재한다)
 * @property migrationMode [String] 마이그레이션 모드(placement_policy<affinity>) VmAffinity (MIGRATABLE, USER_MIGRATABLE, PINNED)
 * @property migrationPolicy [String] 마이그레이션 정책  migration_downtime
 * @property migrationEncrypt [InheritableBoolean] 마이그레이션 암호화 사용
 * @property parallelMigration [String] Parallel Migrations
 *
 * <고가용성>
 * @property ha [Boolean]  					고가용성
 * @property storageDomainVo [IdentifiedVo] 가상 머신 임대 대상 스토리지 도메인
 * @property resumeOperation [String]		재개 동작
 * @property priority [Int]					(true/false는 프론트에서)고가용성 HighAvailability 우선순위 (낮음:1, 중간:50, 높음:100 )
 * @property watchDogModel [String]			워치독 모델 WatchdogModel.I6300ESB
 * @property watchDogAction [WatchdogAction] 워치독 작업
 *
 * <리소스 할당>
 * AutoPinningPolicy(adjust, disabled, existing)
 * CpuPinningPolicy(dedicated, manual(안됨), none, resize_and_pin_numa);
 * CpuShare(비활성화, 비활성화됨(0), 낮음(512), 중간(1024), 높음(2048), 사용자 지정)
 * @property cpuProfileVo [IdentifiedVo] 			CPU 프로파일
 * @property cpuShare [Int] 				CPU 공유
 * @property cpuPinningPolicy [String] 		CPU Pinning Policy
 * @property memoryBalloon [Boolean] 		메모리 balloon 활성화
 * @property ioThreadCnt [Int] 				I/O 스레드 활성화
 * @property multiQue [Boolean] 			멀티 큐 사용
 * @property virtSCSIEnable [Boolean] 		VirtIO-SCSI 활성화
 * @property virtIoCnt [String] 			VirtIO-SCSI Multi Queues
 *
 * <부트 옵션>
 * CDROM("cdrom"), HD("hd"), NETWORK("network");
 * @property firstDevice [String]
 * @property secDevice [String]
 * @property deviceList List<[String]>
 * @property connVo [IdentifiedVo] cd/dvd 연결되면 뜰 iso id (사실 디스크 id)
 * @property bootingMenu [Boolean]
 */
class TemplateVo(
	val id: String = "",
	val name: String = "",
	val status: TemplateStatus = TemplateStatus.OK,
	val upTime: String = "",
	val creationTime: Date = Date(),
	val memoryInstalled: BigInteger = BigInteger.ZERO,
	val memoryUsed: BigInteger = BigInteger.ZERO,
	val memoryBuffered: BigInteger = BigInteger.ZERO,
	val memoryCached: BigInteger = BigInteger.ZERO,
	val memoryFree: BigInteger = BigInteger.ZERO,
	val memoryUnused: BigInteger = BigInteger.ZERO,
	val fqdn: String = "",
	val ipv4: String = "",
	val ipv6: String = "",
	val placement: String = "",
	val hostVo: IdentifiedVo = IdentifiedVo(),
	val snapshotVos: List<IdentifiedVo> = listOf(),
	val nicVos: List<NicVo> = listOf(),
	val vmVo: IdentifiedVo = IdentifiedVo(),
	val dataCenterVo: IdentifiedVo = IdentifiedVo(),
	val clusterVo: IdentifiedVo = IdentifiedVo(),
	val versionName: String = "",
	val versionNum: Int = 0,
	val baseTemplate: IdentifiedVo = IdentifiedVo(),
	val description: String = "",
	val comment: String = "",
	val osSystem: String = "",
	val chipsetFirmwareType: String = "",
	val optimizeOption: String = "",
	val stateless: Boolean = false,
	val startPaused: Boolean = false,
	val deleteProtected: Boolean = false,
	val diskAttachmentVos: List<DiskAttachmentVo> = listOf(),
	val vnicProfileVos: List<VnicProfileVo> = listOf(),
	val memorySize: BigInteger = BigInteger.ZERO,
	val memoryMax: BigInteger = BigInteger.ZERO,
	val memoryActual: BigInteger = BigInteger.ZERO,
	val cpuArc: Architecture = Architecture.UNDEFINED,
	val cpuTopologyCnt: Int = 0,
	val cpuTopologyCore: Int = 0,
	val cpuTopologySocket: Int = 0,
	val cpuTopologyThread: Int = 0,
	val instanceType: String = "",
	val timeOffset: String = "",
	val cloudInit: Boolean = false,
	val hostName: String = "",
	val timeStandard: String = "",
	val script: String = "",
	val monitor: Int = 0,
	val usb: Boolean = false,
	val hostInCluster: Boolean = false,
	val hostVos: List<IdentifiedVo> = listOf(),
	val migrationMode: String = "",
	val migrationPolicy: String = "",
	val migrationEncrypt: InheritableBoolean = InheritableBoolean.INHERIT,
	val parallelMigration: String = "",
	val ha: Boolean = false,
	val storageDomainVo: IdentifiedVo = IdentifiedVo(),
	val resumeOperation: String = "",
	val priority: Int = 0,
	val watchDogModel: String = "",
	val watchDogAction: WatchdogAction = WatchdogAction.NONE,
	val cpuProfileVo: IdentifiedVo = IdentifiedVo(),
	val cpuShare: Int = 0,
	val cpuPinningPolicy: String = "",
	val memoryBalloon: Boolean = false,
	val ioThreadCnt: Int = 0,
	val multiQue: Boolean = false,
	val virtSCSIEnable: Boolean = false,
	val virtIoCnt: String = "",
	val firstDevice: String = "",
	val secDevice: String = "",
	val deviceList: List<String> = listOf(),
	val connVo: IdentifiedVo = IdentifiedVo(),
	val bootingMenu: Boolean = false
): Serializable {
	override fun toString(): String =
		gson.toJson(this)
		
	class Builder {
		private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: ""}
		private var bName: String = ""; fun name(block: () -> String?) { bName = block() ?: ""}
		private var bStatus: TemplateStatus = TemplateStatus.OK; fun status(block: () -> TemplateStatus?) { bStatus = block() ?: TemplateStatus.OK }
		private var bUpTime: String = ""; fun upTime(block: () -> String?) { bUpTime = block() ?: "" }
		private var bCreationTime: Date = Date(); fun creationTime(block: () -> Date?) { bCreationTime = block() ?: Date() }
		private var bMemoryInstalled: BigInteger = BigInteger.ZERO; fun memoryInstalled(block: () -> BigInteger?) { bMemoryInstalled = block() ?: BigInteger.ZERO }
		private var bMemoryUsed: BigInteger = BigInteger.ZERO; fun memoryUsed(block: () -> BigInteger?) { bMemoryUsed = block() ?: BigInteger.ZERO }
		private var bMemoryBuffered: BigInteger = BigInteger.ZERO; fun memoryBuffered(block: () -> BigInteger?) { bMemoryBuffered = block() ?: BigInteger.ZERO }
		private var bMemoryCached: BigInteger = BigInteger.ZERO; fun memoryCached(block: () -> BigInteger?) { bMemoryCached = block() ?: BigInteger.ZERO }
		private var bMemoryFree: BigInteger = BigInteger.ZERO; fun memoryFree(block: () -> BigInteger?) { bMemoryFree = block() ?: BigInteger.ZERO }
		private var bMemoryUnused: BigInteger = BigInteger.ZERO; fun memoryUnused(block: () -> BigInteger?) { bMemoryUnused = block() ?: BigInteger.ZERO }
		private var bFqdn: String = ""; fun fqdn(block: () -> String?) { bFqdn = block() ?: "" }
		private var bIpv4: String = ""; fun ipv4(block: () -> String?) { bIpv4 = block() ?: "" }
		private var bIpv6: String = ""; fun ipv6(block: () -> String?) { bIpv6 = block() ?: "" }
		private var bPlacement: String = ""; fun placement(block: () -> String?) { bPlacement = block() ?: "" }
		private var bHostVo: IdentifiedVo = IdentifiedVo(); fun hostVo(block: () -> IdentifiedVo?) { bHostVo = block() ?: IdentifiedVo() }
		private var bSnapshotVos: List<IdentifiedVo> = listOf(); fun snapshotVos(block: () -> List<IdentifiedVo>?) { bSnapshotVos = block() ?: listOf() }
		private var bNicVos: List<NicVo> = listOf(); fun nicVos(block: () -> List<NicVo>?) { bNicVos = block() ?: listOf() }
		private var bVmVo: IdentifiedVo = IdentifiedVo(); fun vmVo(block: () -> IdentifiedVo?) { bVmVo = block() ?: IdentifiedVo() }
		private var bDataCenterVo: IdentifiedVo = IdentifiedVo(); fun dataCenterVo(block: () -> IdentifiedVo?) { bDataCenterVo = block() ?: IdentifiedVo() }
		private var bClusterVo: IdentifiedVo = IdentifiedVo(); fun clusterVo(block: () -> IdentifiedVo?) { bClusterVo = block() ?: IdentifiedVo() }
		private var bVersionName: String = "";fun versionName(block: () -> String?) { bVersionName = block() ?: "" }
		private var bVersionNum: Int = 0;fun versionNum(block: () -> Int?) { bVersionNum = block() ?: 0 }
		private var bBaseTemplate: IdentifiedVo = IdentifiedVo();fun baseTemplate(block: () -> IdentifiedVo?) { bBaseTemplate = block() ?: IdentifiedVo() }
		private var bDescription: String = ""; fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bComment: String = ""; fun comment(block: () -> String?) { bComment = block() ?: "" }
		private var bOsSystem: String = ""; fun osSystem(block: () -> String?) { bOsSystem = block() ?: "" }
		private var bChipsetFirmwareType: String = ""; fun chipsetFirmwareType(block: () -> String?) { bChipsetFirmwareType = block() ?: "" }
		private var bOptimizeOption: String = ""; fun optimizeOption(block: () -> String?) { bOptimizeOption = block() ?: "" }
		private var bStateless: Boolean = false; fun stateless(block: () -> Boolean?) { bStateless = block() ?: false }
		private var bStartPaused: Boolean = false; fun startPaused(block: () -> Boolean?) { bStartPaused = block() ?: false }
		private var bDeleteProtected: Boolean = false; fun deleteProtected(block: () -> Boolean?) { bDeleteProtected = block() ?: false }
		private var bDiskAttachmentVos: List<DiskAttachmentVo> = listOf(); fun diskAttachmentVos(block: () -> List<DiskAttachmentVo>?) { bDiskAttachmentVos = block() ?: listOf() }
		private var bVnicProfileVos: List<VnicProfileVo> = listOf(); fun vnicProfileVos(block: () -> List<VnicProfileVo>?) { bVnicProfileVos = block() ?: listOf() }
		private var bMemorySize: BigInteger = BigInteger.ZERO; fun memorySize(block: () -> BigInteger?) { bMemorySize = block() ?: BigInteger.ZERO }
		private var bMemoryMax: BigInteger = BigInteger.ZERO; fun memoryMax(block: () -> BigInteger?) { bMemoryMax = block() ?: BigInteger.ZERO }
		private var bMemoryActual: BigInteger = BigInteger.ZERO; fun memoryActual(block: () -> BigInteger?) { bMemoryActual = block() ?: BigInteger.ZERO }
		private var bCpuArc: Architecture = Architecture.UNDEFINED; fun cpuArc(block: () -> Architecture?) { bCpuArc = block() ?: Architecture.UNDEFINED }
		private var bCpuTopologyCnt: Int = 0; fun cpuTopologyCnt(block: () -> Int?) { bCpuTopologyCnt = block() ?: 0 }
		private var bCpuTopologyCore: Int = 0; fun cpuTopologyCore(block: () -> Int?) { bCpuTopologyCore = block() ?: 0 }
		private var bCpuTopologySocket: Int = 0; fun cpuTopologySocket(block: () -> Int?) { bCpuTopologySocket = block() ?: 0 }
		private var bCpuTopologyThread: Int = 0; fun cpuTopologyThread(block: () -> Int?) { bCpuTopologyThread = block() ?: 0 }
		private var bInstanceType: String = ""; fun instanceType(block: () -> String?) { bInstanceType = block() ?: "" }
		private var bTimeOffset: String = ""; fun timeOffset(block: () -> String?) { bTimeOffset = block() ?: "" }
		private var bCloudInit: Boolean = false; fun cloudInit(block: () -> Boolean?) { bCloudInit = block() ?: false }
		private var bHostName: String = ""; fun hostName(block: () -> String?) { bHostName = block() ?: "" }
		private var bTimeStandard: String = ""; fun timeStandard(block: () -> String?) { bTimeStandard = block() ?: "" }
		private var bScript: String = ""; fun script(block: () -> String?) { bScript = block() ?: "" }
		private var bMonitor: Int = 0; fun monitor(block: () -> Int?) { bMonitor = block() ?: 0 }
		private var bUsb: Boolean = false; fun usb(block: () -> Boolean?) { bUsb = block() ?: false }
		private var bHostInCluster: Boolean = false; fun hostInCluster(block: () -> Boolean?) { bHostInCluster = block() ?: false }
		private var bHostVos: List<IdentifiedVo> = listOf(); fun hostVos(block: () -> List<IdentifiedVo>?) { bHostVos = block() ?: listOf() }
		private var bMigrationMode: String = ""; fun migrationMode(block: () -> String?) { bMigrationMode = block() ?: "" }
		private var bMigrationPolicy: String = ""; fun migrationPolicy(block: () -> String?) { bMigrationPolicy = block() ?: "" }
		private var bMigrationEncrypt: InheritableBoolean = InheritableBoolean.INHERIT; fun migrationEncrypt(block: () -> InheritableBoolean?) { bMigrationEncrypt = block() ?: InheritableBoolean.INHERIT }
		private var bParallelMigration: String = ""; fun parallelMigration(block: () -> String?) { bParallelMigration = block() ?: "" }
		private var bHa: Boolean = false; fun ha(block: () -> Boolean?) { bHa = block() ?: false }
		private var bStorageDomainVo: IdentifiedVo = IdentifiedVo(); fun storageDomainVo(block: () -> IdentifiedVo?) { bStorageDomainVo = block() ?: IdentifiedVo() }
		private var bResumeOperation: String = ""; fun resumeOperation(block: () -> String?) { bResumeOperation = block() ?: "" }
		private var bPriority: Int = 0; fun priority(block: () -> Int?) { bPriority = block() ?: 0 }
		private var bWatchDogModel: String = ""; fun watchDogModel(block: () -> String?) { bWatchDogModel = block() ?: "" }
		private var bWatchDogAction: WatchdogAction = WatchdogAction.NONE; fun watchDogAction(block: () -> WatchdogAction?) { bWatchDogAction = block() ?: WatchdogAction.NONE }
		private var bCpuProfileVo: IdentifiedVo = IdentifiedVo(); fun cpuProfileVo(block: () -> IdentifiedVo?) { bCpuProfileVo = block() ?: IdentifiedVo() }
		private var bCpuShare: Int = 0; fun cpuShare(block: () -> Int?) { bCpuShare = block() ?: 0 }
		private var bCpuPinningPolicy: String = ""; fun cpuPinningPolicy(block: () -> String?) { bCpuPinningPolicy = block() ?: "" }
		private var bMemoryBalloon: Boolean = false; fun memoryBalloon(block: () -> Boolean?) { bMemoryBalloon = block() ?: false }
		private var bIoThreadCnt: Int = 0; fun ioThreadCnt(block: () -> Int?) { bIoThreadCnt = block() ?: 0 }
		private var bMultiQue: Boolean = false; fun multiQue(block: () -> Boolean?) { bMultiQue = block() ?: false }
		private var bVirtSCSIEnable: Boolean = false; fun virtSCSIEnable(block: () -> Boolean?) { bVirtSCSIEnable = block() ?: false }
		private var bVirtIoCnt: String = ""; fun virtIoCnt(block: () -> String?) { bVirtIoCnt = block() ?: "" }
		private var bFirstDevice: String = ""; fun firstDevice(block: () -> String?) { bFirstDevice = block() ?: "" }
		private var bSecDevice: String = ""; fun secDevice(block: () -> String?) { bSecDevice = block() ?: "" }
		private var bDeviceList: List<String> = listOf(); fun deviceList(block: () -> List<String>?) { bDeviceList = block() ?: listOf() }
		private var bConnVo: IdentifiedVo = IdentifiedVo(); fun connVo(block: () -> IdentifiedVo?) { bConnVo = block() ?: IdentifiedVo() }
		private var bBootingMenu: Boolean = false; fun bootingMenu(block: () -> Boolean?) { bBootingMenu = block() ?: false }

		fun build(): TemplateVo = TemplateVo(bId, bName, bStatus, bUpTime, bCreationTime, bMemoryInstalled, bMemoryUsed, bMemoryBuffered, bMemoryCached, bMemoryFree, bMemoryUnused, bFqdn, bIpv4, bIpv6, bPlacement, bHostVo, bSnapshotVos, bNicVos, bVmVo, bDataCenterVo, bClusterVo, bVersionName, bVersionNum, bBaseTemplate, bDescription, bComment, bOsSystem, bChipsetFirmwareType, bOptimizeOption, bStateless, bStartPaused, bDeleteProtected, bDiskAttachmentVos, bVnicProfileVos, bMemorySize, bMemoryMax, bMemoryActual, bCpuArc, bCpuTopologyCnt, bCpuTopologyCore, bCpuTopologySocket, bCpuTopologyThread, /*bUserEmulation, bUserCpu, bUserVersion,*/ bInstanceType, bTimeOffset, bCloudInit, bHostName, bTimeStandard, bScript, bMonitor, bUsb, bHostInCluster, bHostVos, bMigrationMode, bMigrationPolicy, bMigrationEncrypt, bParallelMigration, bHa, bStorageDomainVo, bResumeOperation, bPriority, bWatchDogModel, bWatchDogAction, bCpuProfileVo, bCpuShare, bCpuPinningPolicy, bMemoryBalloon, bIoThreadCnt, bMultiQue, bVirtSCSIEnable, bVirtIoCnt, bFirstDevice, bSecDevice, bDeviceList, bConnVo, bBootingMenu)
	}

	companion object {
		inline fun builder(block: TemplateVo.Builder.() -> Unit): TemplateVo = TemplateVo.Builder().apply(block).build()
	}
}


fun Template.toTemplateMenu(conn: Connection): TemplateVo {
	val cluster: Cluster? =
		if(this@toTemplateMenu.clusterPresent()) conn.findCluster(this@toTemplateMenu.cluster().id()).getOrNull()
		else null
	val dataCenter: DataCenter? = cluster?.dataCenter()?.let { conn.findDataCenter(it.id()).getOrNull() }

	return TemplateVo.builder {
		id { this@toTemplateMenu.id() }
		name { this@toTemplateMenu.name() }
		description { this@toTemplateMenu.description() }
		creationTime { this@toTemplateMenu.creationTime() }
		status { this@toTemplateMenu.status() }
		clusterVo { cluster?.fromClusterToIdentifiedVo() }
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
	}
}
fun List<Template>.toTemplatesMenu(conn: Connection): List<TemplateVo> =
	this@toTemplatesMenu.map { it.toTemplateMenu(conn) }


fun Template.toTemplateInfo(conn: Connection): TemplateVo {
	val cluster: Cluster? =
		if(this@toTemplateInfo.clusterPresent()) {
			conn.findCluster(this@toTemplateInfo.cluster().id()).getOrNull()
		}else{ null }

	return TemplateVo.builder {
		id { this@toTemplateInfo.id() }
		name { this@toTemplateInfo.name() }
		description { this@toTemplateInfo.description() }
		status { this@toTemplateInfo.status() }
		versionName { if (this@toTemplateInfo.versionPresent()) this@toTemplateInfo.version().versionName() else "" }
		versionNum { if (this@toTemplateInfo.versionPresent()) this@toTemplateInfo.version().versionNumberAsInteger() else 0 }
		creationTime { this@toTemplateInfo.creationTime() }
		osSystem { if (this@toTemplateInfo.osPresent()) Os.findByCode(this@toTemplateInfo.os().type()).fullName else null }
		chipsetFirmwareType { if (this@toTemplateInfo.bios().typePresent()) this@toTemplateInfo.bios().type().findBios() else null }
		optimizeOption { this@toTemplateInfo.type().findVmType() } // 최적화 옵션
		memorySize { this@toTemplateInfo.memory() }
		cpuTopologyCore { this@toTemplateInfo.cpu().topology().coresAsInteger() }
		cpuTopologySocket { this@toTemplateInfo.cpu().topology().socketsAsInteger() }
		cpuTopologyThread { this@toTemplateInfo.cpu().topology().threadsAsInteger() }
		cpuTopologyCnt {
			this@toTemplateInfo.cpu().topology().coresAsInteger() *
					this@toTemplateInfo.cpu().topology().socketsAsInteger() *
					this@toTemplateInfo.cpu().topology().threadsAsInteger()
		}
		monitor { this@toTemplateInfo.display().monitorsAsInteger() }
		ha { this@toTemplateInfo.highAvailability().enabled() }
		priority { this@toTemplateInfo.highAvailability().priorityAsInteger() }
		usb { this@toTemplateInfo.usb().enabled() }
		clusterVo { cluster?.fromClusterToIdentifiedVo() }
	}
}

/**
 * 템플릿 빌더
 */
fun TemplateVo.toTemplateBuilder(): TemplateBuilder {
	return TemplateBuilder()
		.name(this@toTemplateBuilder.name)
		.description(this@toTemplateBuilder.description)
		.cluster(ClusterBuilder().id(this@toTemplateBuilder.clusterVo.id))
}

/**
 * <template>
 *   <name>mytemplate</name>
 *   <vm id="123">
 *     <disk_attachments>
 *       <disk_attachment>
 *         <disk id="456">
 *           <name>mydisk</name>
 *           <format>cow</format>
 *           <sparse>true</sparse>
 *         </disk>
 *       </disk_attachment>
 *     </disk_attachments>
 *   </vm>
 * </template>
 */
fun TemplateVo.toAddTemplateBuilder(): Template {
	val diskAttachments: List<DiskAttachment> =
		this@toAddTemplateBuilder.diskAttachmentVos.map { disk ->
			DiskAttachmentBuilder()
				.disk(
					DiskBuilder()
						.id(disk.diskImageVo.id)
						.format(disk.diskImageVo.format)
						.storageDomains(*arrayOf(StorageDomainBuilder().id(disk.diskImageVo.storageDomainVo.id).build()))
						.diskProfile(DiskProfileBuilder().id(disk.diskImageVo.diskProfileVo.id).build())
						.build()
				).build()
		}

	return TemplateBuilder()
		.comment(this@toAddTemplateBuilder.comment)
		.cpuProfile(CpuProfileBuilder().id(this@toAddTemplateBuilder.cpuProfileVo.id))
		.vm(VmBuilder().id(this@toAddTemplateBuilder.vmVo.id))
		.diskAttachments(diskAttachments)
		.build()
}

fun TemplateVo.toEditTemplateBuilder(): Template {
	return TemplateBuilder()
		.id(this@toEditTemplateBuilder.id)
		.os(OperatingSystemBuilder().type(this@toEditTemplateBuilder.osSystem))
		.bios(BiosBuilder().type(BiosType.valueOf(this@toEditTemplateBuilder.chipsetFirmwareType)))
		.type(VmType.valueOf(this@toEditTemplateBuilder.optimizeOption))
		.build()
}
