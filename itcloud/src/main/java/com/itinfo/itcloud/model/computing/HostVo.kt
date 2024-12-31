package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.gson
import com.itinfo.itcloud.ovirtDf
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.network.toHostNicVos
import com.itinfo.itcloud.model.network.toSlaveHostNicVos
import com.itinfo.itcloud.repository.history.dto.UsageDto
import com.itinfo.itcloud.repository.history.entity.HostConfigurationEntity
import com.itinfo.util.ovirt.*
import org.slf4j.LoggerFactory
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.*
import java.io.Serializable
import java.math.BigInteger
import java.util.*

private val log = LoggerFactory.getLogger(HostVo::class.java)

/**
 * [HostVo]
 * 호스트
 *
 * @property id [String]
 * @property name [String]
 * @property comment [String]
 * @property address [String]  호스트 ip
 * @property devicePassThrough [Boolean]
 * @property hostedActive [Boolean] 활성여부
 * @property hostedScore [Int] 점수
 * @property iscsi [String]
 * @property kdump  [KdumpStatus]   kdumpStatus(disabled, enabled, unknown)
 * @property ksm [Boolean]  hosted engine
 * @property seLinux [SeLinuxMode] SeLinuxMode(disabled, enforcing, permissive)
 * @property hostedEngine [Boolean] Hosted Engine 이동 여부 [ 금장, 은장, null ]
 * @property hostedEngineVM [Boolean] Hosted Engine VM 여부 [ 금장, 은장, null ]
 * @property spmPriority [Int] spm 우선순위
 * @property spmStatus [SpmStatus] spm 상태
 * @property sshFingerPrint [String] ssh
 * @property sshPort [Int]
 * @property sshPublicKey[String]
 * @property sshName [String] 사용자 이름 (생성시 표시)
 * @property sshPassWord [String] 암호 (생성시)
 * @property status [HostStatus]
 * @property transparentPage [Boolean] 자동으로 페이지를 크게
 * @property vmTotalCnt [Int] summary
 * @property vmActiveCnt [Int] summary
 * @property vmSizeVo [SizeVo]
 * @property vmMigratingCnt [Int] summary
 * @property vgpu [String]   VgpuPlacement
 * 전원관리는 항상 비활성상태
 * <statistics>
 * @property memoryTotal [BigInteger]
 * @property memoryUsed [BigInteger]
 * @property memoryFree [BigInteger]
 * @property memoryMax [BigInteger] 새 가상머신 최대여유메모리
 * @property memoryShared [BigInteger] 공유 메모리
 * @property swapTotal [BigInteger]
 * @property swapUsed [BigInteger]
 * @property swapFree [BigInteger]
 * @property hugePage2048Free [Int] Huge Pages (size: free/total) 2048:0/0, 1048576:0/0
 * @property hugePage2048Total [Int]
 * @property hugePage1048576Free [Int]
 * @property hugePage1048576Total [Int]
 * @property bootingTime [String]
 * @property hostHwVo [HostHwVo] 호스트 하드웨어
 * @property hostSwVo [HostSwVo] 호스트 소프트웨어
 * @property clusterVo [ClusterVo]
 * @property dataCenterVo [DataCenterVo]
 * @property hostNicVos List<[HostNicVo]>
 * @property vmVos List<[VmVo]>
 * @property usageDto [UsageDto]
 */
class HostVo (
    val id: String = "",
    val name: String = "",
    val comment: String = "",
    val address: String = "",
    val devicePassThrough: Boolean = false,
    val hostedActive: Boolean = false,
    val hostedScore: Int = 0,
    val iscsi: String = "",
    val kdump: KdumpStatus = KdumpStatus.UNKNOWN,
    val ksm: Boolean = false,
    val seLinux: SeLinuxMode = SeLinuxMode.DISABLED,
    val hostedEngine: Boolean = false,
    val hostedEngineVM: Boolean = false,
    val spmPriority: Int = 0,
    val spmStatus: SpmStatus = SpmStatus.NONE,
    val sshFingerPrint: String = "",
    val sshPort: Int = 0,
    val sshPublicKey: String = "",
    val sshName: String = "",
    val sshPassWord: String = "",
    val status: HostStatus = HostStatus.NON_RESPONSIVE,
    val transparentPage: Boolean = false,
    val vmSizeVo: SizeVo = SizeVo(),
    val vmMigratingCnt: Int = 0,
    val vgpu: String = "", /*VgpuPlacement*/
    val memoryTotal: BigInteger = BigInteger.ZERO,
    val memoryUsed: BigInteger = BigInteger.ZERO,
    val memoryFree: BigInteger = BigInteger.ZERO,
    val memoryMax: BigInteger = BigInteger.ZERO,
    val memoryShared: BigInteger = BigInteger.ZERO,
    val swapTotal: BigInteger = BigInteger.ZERO,
    val swapUsed: BigInteger = BigInteger.ZERO,
    val swapFree: BigInteger = BigInteger.ZERO,
    val hugePage2048Free: Int = 0,
    val hugePage2048Total: Int = 0,
    val hugePage1048576Free: Int = 0,
    val hugePage1048576Total: Int = 0,
    val bootingTime: String = "",
    val hostHwVo: HostHwVo = HostHwVo(),
    val hostSwVo: HostSwVo = HostSwVo(),
    val clusterVo: IdentifiedVo = IdentifiedVo(),
    val dataCenterVo: IdentifiedVo = IdentifiedVo(),
    val hostNicVos: List<HostNicVo> = listOf(),
    val vmVos: List<IdentifiedVo> = listOf(),
    val usageDto: UsageDto = UsageDto(),

): Serializable{
    override fun toString(): String = gson.toJson(this)

    class Builder{
        private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: ""}
        private var bName: String = ""; fun name(block: () -> String?) { bName = block() ?: ""}
        private var bComment: String = ""; fun comment(block: () -> String?) { bComment = block() ?: ""}
        private var bAddress: String = ""; fun address(block: () -> String?) { bAddress = block() ?: ""}
        private var bDevicePassThrough: Boolean = false; fun devicePassThrough(block: () -> Boolean?) { bDevicePassThrough = block() ?: false}
        private var bHostedActive: Boolean = false; fun hostedActive(block: () -> Boolean?) { bHostedActive = block() ?: false }
        private var bHostedScore: Int = 0; fun hostedScore(block: () -> Int?) { bHostedScore = block() ?: 0 }
        private var bIscsi: String = ""; fun iscsi (block: () -> String?) { bIscsi = block() ?: ""}
        private var bKdump: KdumpStatus = KdumpStatus.UNKNOWN; fun kdump (block: () -> KdumpStatus?) { bKdump = block() ?: KdumpStatus.UNKNOWN}
        private var bKsm: Boolean = false; fun ksm(block: () -> Boolean?) { bKsm = block() ?: false }
        private var bSeLinux: SeLinuxMode = SeLinuxMode.DISABLED; fun seLinux(block: () -> SeLinuxMode?) { bSeLinux = block() ?: SeLinuxMode.DISABLED }
        private var bHostedEngine: Boolean = false; fun hostedEngine(block: () -> Boolean?) { bHostedEngine = block() ?: false }
        private var bHostedEngineVM: Boolean = false; fun hostedEngineVM(block: () -> Boolean?) { bHostedEngineVM = block() ?: false }
        private var bSpmPriority: Int = 0; fun spmPriority(block: () -> Int?) { bSpmPriority = block() ?: 0 }
        private var bSpmStatus: SpmStatus = SpmStatus.NONE; fun spmStatus(block: () -> SpmStatus?) { bSpmStatus = block() ?: SpmStatus.NONE }
        private var bSshFingerPrint: String = ""; fun sshFingerPrint(block: () -> String?) { bSshFingerPrint = block() ?: "" }
        private var bSshPort: Int = 0; fun sshPort(block: () -> Int?) { bSshPort = block() ?: 0 }
        private var bSshPublicKey: String = ""; fun sshPublicKey(block: () -> String?) { bSshPublicKey = block() ?: "" }
        private var bSshName: String = ""; fun sshName(block: () -> String?) { bSshName = block() ?: "" }
        private var bSshPassWord: String = ""; fun sshPassWord(block: () -> String?) { bSshPassWord = block() ?: "" }
        private var bStatus: HostStatus = HostStatus.NON_RESPONSIVE; fun status(block: () -> HostStatus?) { bStatus = block() ?: HostStatus.NON_RESPONSIVE }
        private var bTransparentPage: Boolean = false; fun transparentPage(block: () -> Boolean?) { bTransparentPage = block() ?: false }
//        private var bVmTotalCnt: Int = 0; fun vmTotalCnt(block: () -> Int?) { bVmTotalCnt = block() ?: 0 }
//        private var bVmActiveCnt: Int = 0; fun vmActiveCnt(block: () -> Int?) { bVmActiveCnt = block() ?: 0 }
        private var bVmSizeVo: SizeVo = SizeVo(); fun vmSizeVo(block: () -> SizeVo?) { bVmSizeVo = block() ?: SizeVo() }
        private var bVmMigratingCnt: Int = 0; fun vmMigratingCnt(block: () -> Int?) { bVmMigratingCnt = block() ?: 0 }
        private var bVgpu: String = ""; fun vgpu(block: () -> String?) { bVgpu = block() ?: "" }
        private var bMemoryTotal: BigInteger = BigInteger.ZERO; fun memoryTotal (block: () -> BigInteger?) { bMemoryTotal = block() ?: BigInteger.ZERO}
        private var bMemoryUsed: BigInteger = BigInteger.ZERO; fun memoryUsed (block: () -> BigInteger?) { bMemoryUsed = block() ?: BigInteger.ZERO}
        private var bMemoryFree: BigInteger = BigInteger.ZERO; fun memoryFree (block: () -> BigInteger?) { bMemoryFree = block() ?: BigInteger.ZERO}
        private var bMemoryMax: BigInteger = BigInteger.ZERO; fun memoryMax (block: () -> BigInteger?) { bMemoryMax = block() ?: BigInteger.ZERO}
        private var bMemoryShared: BigInteger = BigInteger.ZERO; fun memoryShared (block: () -> BigInteger?) { bMemoryShared = block() ?: BigInteger.ZERO}
        private var bSwapTotal: BigInteger = BigInteger.ZERO; fun swapTotal (block: () -> BigInteger?) { bSwapTotal = block() ?: BigInteger.ZERO}
        private var bSwapUsed: BigInteger = BigInteger.ZERO; fun swapUsed (block: () -> BigInteger?) { bSwapUsed = block() ?: BigInteger.ZERO}
        private var bSwapFree: BigInteger = BigInteger.ZERO; fun swapFree (block: () -> BigInteger?) { bSwapFree = block() ?: BigInteger.ZERO}
        private var bHugePage2048Free: Int = 0; fun hugePage2048Free(block: () -> Int?) { bHugePage2048Free = block() ?: 0}
        private var bHugePage2048Total: Int = 0; fun hugePage2048Total(block: () -> Int?) { bHugePage2048Total = block() ?: 0}
        private var bHugePage1048576Free: Int = 0; fun hugePage1048576Free(block: () -> Int?) { bHugePage1048576Free = block() ?: 0}
        private var bHugePage1048576Total: Int = 0; fun hugePage1048576Total(block: () -> Int?) { bHugePage1048576Total = block() ?: 0}
        private var bBootingTime: String = ""; fun bootingTime (block: () -> String?) { bBootingTime = block() ?: ""}
        private var bHostHwVo: HostHwVo = HostHwVo(); fun hostHwVo(block: () -> HostHwVo?) { bHostHwVo = block() ?: HostHwVo() }
        private var bHostSwVo: HostSwVo = HostSwVo(); fun hostSwVo(block: () -> HostSwVo?) { bHostSwVo = block() ?: HostSwVo()}
        private var bClusterVo: IdentifiedVo = IdentifiedVo(); fun clusterVo(block: () -> IdentifiedVo?) { bClusterVo = block() ?: IdentifiedVo() }
        private var bDataCenterVo: IdentifiedVo = IdentifiedVo(); fun dataCenterVo(block: () -> IdentifiedVo?) { bDataCenterVo = block() ?: IdentifiedVo() }
        private var bHostNicVos: List<HostNicVo> = listOf(); fun hostNicVos(block: () -> List<HostNicVo>?) { bHostNicVos = block() ?: listOf() }
        private var bVmVos: List<IdentifiedVo> = listOf(); fun vmVos(block: () -> List<IdentifiedVo>?) { bVmVos = block() ?: listOf() }
        private var bUsageDto: UsageDto = UsageDto(); fun usageDto(block: () -> UsageDto?) { bUsageDto = block() ?: UsageDto() }

        fun build(): HostVo = HostVo(bId, bName, bComment, bAddress, bDevicePassThrough, bHostedActive, bHostedScore, bIscsi, bKdump, bKsm, bSeLinux, bHostedEngine, bHostedEngineVM, bSpmPriority, bSpmStatus, bSshFingerPrint, bSshPort, bSshPublicKey, bSshName, bSshPassWord, bStatus, bTransparentPage, /*bVmTotalCnt, bVmActiveCnt,*/ bVmSizeVo, bVmMigratingCnt, bVgpu, bMemoryTotal, bMemoryUsed, bMemoryFree, bMemoryMax, bMemoryShared, bSwapTotal, bSwapUsed, bSwapFree, bHugePage2048Free, bHugePage2048Total, bHugePage1048576Free, bHugePage1048576Total, bBootingTime, bHostHwVo, bHostSwVo, bClusterVo, bDataCenterVo, bHostNicVos, bVmVos, bUsageDto)
    }
    companion object {
        inline fun builder(block: HostVo.Builder.() -> Unit): HostVo = HostVo.Builder().apply(block).build()
    }
}

/**
 * 클러스터 id&name
 */
fun Host.toHostIdName(): HostVo = HostVo.builder {
    id { this@toHostIdName.id() }
    name { this@toHostIdName.name() }
}
fun List<Host>.toHostsIdName(): List<HostVo> =
    this@toHostsIdName.map { it.toHostIdName() }

/**
 * 호스트 목록
 */
fun Host.toHostMenu(conn: Connection, usageDto: UsageDto?): HostVo {
    val cluster: Cluster? = conn.findCluster(this@toHostMenu.cluster().id())
        .getOrNull()

    val dataCenter: DataCenter? = cluster?.dataCenter()?.id()?.let {
        conn.findDataCenter(it).getOrNull()
    }

    val hostedVm = conn.findAllVmsFromHost(this@toHostMenu.id())
        .getOrDefault(listOf())
        .any { it.origin() == "managed_hosted_engine" }

    return HostVo.builder {
        id { this@toHostMenu.id() }
        name { this@toHostMenu.name() }
        comment { this@toHostMenu.comment() }
        status { this@toHostMenu.status() }
//        ksm { this@toHostMenu.ksm().enabled() }
        hostedEngine { this@toHostMenu.hostedEnginePresent() }
        hostedEngineVM { hostedVm }
        address { this@toHostMenu.address() }
        clusterVo { cluster?.fromClusterToIdentifiedVo() }
        dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
        vmSizeVo {
            SizeVo.builder {
                allCnt { if(this@toHostMenu.summary().totalPresent()) this@toHostMenu.summary().totalAsInteger() else 0 }
                upCnt { if(this@toHostMenu.summary().activePresent()) this@toHostMenu.summary().activeAsInteger() else 0 }
                downCnt { if(this@toHostMenu.summary().activePresent() && this@toHostMenu.summary().totalPresent()) this@toHostMenu.summary().totalAsInteger() - this@toHostMenu.summary().activeAsInteger() else 0}
            }
        }
        usageDto { usageDto }
        spmStatus { this@toHostMenu.spm().status() }
    }
}


fun Host.toHostInfo(conn: Connection, hostConfigurationEntity: HostConfigurationEntity): HostVo {
    val statistics: List<Statistic> =
        conn.findAllStatisticsFromHost(this@toHostInfo.id()).getOrDefault(listOf())

    return HostVo.builder {
        id { this@toHostInfo.id() }
        name { this@toHostInfo.name() }
        comment { this@toHostInfo.comment() }
        status { this@toHostInfo.status() }
        clusterVo { if(this@toHostInfo.clusterPresent()) conn.findCluster(this@toHostInfo.cluster().id()).getOrNull()?.fromClusterToIdentifiedVo() else null}
        address { this@toHostInfo.address() }
        hostedActive { if(this@toHostInfo.hostedEnginePresent()) this@toHostInfo.hostedEngine().active() else false }
        hostedScore { if(this@toHostInfo.hostedEnginePresent()) this@toHostInfo.hostedEngine().scoreAsInteger() else 0 }
        iscsi { if(this@toHostInfo.iscsiPresent()) this@toHostInfo.iscsi().initiator() else "" }
        kdump { this@toHostInfo.kdumpStatus() }
        ksm { this@toHostInfo.ksm().enabled() }
        seLinux { this@toHostInfo.seLinux().mode() }
//        hostedEngine { this@toHostVo.spm().status().equals(SpmStatus.SPM) } //다시 알아보기 (우선순위 숫자에 따라 다른건지?)
        sshPort { this@toHostInfo.ssh().portAsInteger() }
        spmPriority { this@toHostInfo.spm().priorityAsInteger() }
        vgpu { this@toHostInfo.vgpuPlacement().value() }
        transparentPage { this@toHostInfo.transparentHugePages().enabled() }
        memoryTotal { statistics.findMemory("memory.total") }
        memoryUsed { statistics.findMemory("memory.used") }
        memoryFree { statistics.findMemory("memory.free") }
        memoryMax { this@toHostInfo.maxSchedulingMemory() }
        memoryShared { statistics.findSpeed("memory.shared") } // 문제잇음
        swapTotal { statistics.findSpeed("swap.total") }
        swapFree { statistics.findSpeed("swap.free") }
        swapUsed { statistics.findSpeed("swap.used") }
        hugePage2048Total { statistics.findPage("hugepages.2048.total") }
        hugePage2048Free { statistics.findPage("hugepages.2048.free") }
        hugePage1048576Total { statistics.findPage("hugepages.1048576.total") }
        hugePage1048576Free { statistics.findPage("hugepages.1048576.free") }
        bootingTime { ovirtDf.format(Date(statistics.findBootTime()* 1000)) }
        hostHwVo { this@toHostInfo.toHostHwVo() }
        hostSwVo { this@toHostInfo.toHostSwVo(hostConfigurationEntity) }
        vmSizeVo {
            SizeVo.builder {
                upCnt { if(this@toHostInfo.summary().activePresent()) this@toHostInfo.summary().activeAsInteger() else 0 }
            }
        }
    }
}


/**
 * 네트워크에서 호스트 볼때
 */
fun Host.toNetworkHostVo(conn: Connection): HostVo {
    val cluster: Cluster? = conn.findCluster(this@toNetworkHostVo.cluster().id()).getOrNull()
    val dataCenter: DataCenter? = cluster?.dataCenter()?.id()?.let { conn.findDataCenter(it).getOrNull() }
    val hostNics: List<HostNic> = conn.findAllNicsFromHost(this@toNetworkHostVo.id()).getOrDefault(listOf())

    return HostVo.builder {
        id { this@toNetworkHostVo.id() }
        name { this@toNetworkHostVo.name() }
        status { this@toNetworkHostVo.status() }
        clusterVo { cluster?.fromClusterToIdentifiedVo() }
        dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
        hostNicVos { hostNics.toSlaveHostNicVos(conn) }
    }
}
fun List<Host>.toNetworkHostVos(conn: Connection): List<HostVo> =
    this@toNetworkHostVos.map { it.toNetworkHostVo(conn) }



/**
 * 호스트 빌더
 */
fun HostVo.toHostBuilder(): HostBuilder {
    return HostBuilder()
        .cluster(ClusterBuilder().id(this@toHostBuilder.clusterVo.id))
        .name(this@toHostBuilder.name)
        .comment(this@toHostBuilder.comment)
        .address(this@toHostBuilder.address)
        .ssh(SshBuilder().port(this@toHostBuilder.sshPort)) // 기본값이 22 포트 연결은 더 테스트 해봐야함(ovirt 내에서 한적은 없음)
        .rootPassword(this@toHostBuilder.sshPassWord)   // 비밀번호 잘못되면 보여줄 코드?
        .powerManagement(PowerManagementBuilder().enabled(false)) // 전원관리 비활성화 (기본)
//        .spm(SpmBuilder().priority(this@toHostBuilder.spmPriority))
        .vgpuPlacement(VgpuPlacement.fromValue(this@toHostBuilder.vgpu))
//        .port()
        // ssh port가 22면 .ssh() 설정하지 않아도 알아서 지정됨. port 변경을 cmd 에서만 하심
    // deployHostedEngine은 ext에서
}

/**
 * 호스트 생성 빌더
 */
fun HostVo.toAddHostBuilder(): Host =
    this@toAddHostBuilder.toHostBuilder().build()

/**
 * 호스트 편집 빌더
 */
fun HostVo.toEditHostBuilder(): Host =
    this@toEditHostBuilder.toHostBuilder()
        .id(this@toEditHostBuilder.id)
        .os(OperatingSystemBuilder().customKernelCmdline("vfio_iommu_type1.allow_unsafe_interrupts=1").build()) //?
        .build()


/**
 * 호스트 전체 출력
 */
fun Host.toHostVo(conn: Connection): HostVo {
    val statistics: List<Statistic> =
        conn.findAllStatisticsFromHost(this@toHostVo.id())
            .getOrDefault(listOf())
    val cluster: Cluster? =
        conn.findCluster(this@toHostVo.cluster().id())
            .getOrNull()
    val dataCenter: DataCenter? = cluster?.dataCenter()?.id()?.let {
        conn.findDataCenter(it).getOrNull()
    }
    val vms: List<Vm> =
        conn.findAllVms()
            .getOrDefault(listOf())
            .filter { it.hostPresent() && it.host().id() == this@toHostVo.id() }
    val hostNics: List<HostNic> =
        conn.findAllNicsFromHost(this@toHostVo.id())
            .getOrDefault(listOf())

    return HostVo.builder {
        id { this@toHostVo.id() }
        name { this@toHostVo.name() }
        comment { this@toHostVo.comment() }
        address { this@toHostVo.address() }
        devicePassThrough { this@toHostVo.devicePassthrough().enabled() }
        hostedActive { if(this@toHostVo.hostedEnginePresent()) this@toHostVo.hostedEngine().active() else false }
        hostedScore { if(this@toHostVo.hostedEnginePresent()) this@toHostVo.hostedEngine().scoreAsInteger() else 0 }
        iscsi { if(this@toHostVo.iscsiPresent()) this@toHostVo.iscsi().initiator() else "" }
        kdump { this@toHostVo.kdumpStatus() }
        ksm { this@toHostVo.ksm().enabled() }
        seLinux { this@toHostVo.seLinux().mode() }
//        hostedEngine { this@toHostVo.spm().status().equals(SpmStatus.SPM) } //다시 알아보기 (우선순위 숫자에 따라 다른건지?)
        spmPriority { this@toHostVo.spm().priorityAsInteger() }
        spmStatus { this@toHostVo.spm().status() }
        sshFingerPrint { this@toHostVo.ssh().fingerprint() }
        sshPort { this@toHostVo.ssh().portAsInteger() }
        sshPublicKey { this@toHostVo.ssh().publicKey() }
        status { this@toHostVo.status() }
        transparentPage { this@toHostVo.transparentHugePages().enabled() }
        vmSizeVo {
            if (this@toHostVo.summaryPresent()) {
                val allCnt = if (this@toHostVo.summary().totalPresent()) this@toHostVo.summary().totalAsInteger() else 0
                val upCnt = if (this@toHostVo.summary().activePresent()) this@toHostVo.summary().activeAsInteger() else 0
                val downCnt = allCnt - upCnt
                SizeVo.builder {
                    allCnt { allCnt }
                    upCnt { upCnt }
                    downCnt { downCnt }
                }
            } else null
        }
        vmMigratingCnt { this@toHostVo.summary().migratingAsInteger() }
        memoryTotal { statistics.findMemory("memory.total") }
        memoryUsed { statistics.findMemory("memory.used") }
        memoryFree { statistics.findMemory("memory.free") }
        memoryMax { this@toHostVo.maxSchedulingMemory() }
        memoryShared { statistics.findSpeed("memory.shared") } // 문제잇음
        swapTotal { statistics.findSpeed("swap.total") }
        swapFree { statistics.findSpeed("swap.free") }
        swapUsed { statistics.findSpeed("swap.used") }
        hugePage2048Total { statistics.findPage("hugepages.2048.total") }
        hugePage2048Free { statistics.findPage("hugepages.2048.free") }
        hugePage1048576Total { statistics.findPage("hugepages.1048576.total") }
        hugePage1048576Free { statistics.findPage("hugepages.1048576.free") }
        bootingTime { ovirtDf.format(Date(statistics.findBootTime())) }
        hostHwVo { this@toHostVo.toHostHwVo() }
//        hostSwVo { this@toHostVo.toHostSwVo() }
        clusterVo { cluster?.fromClusterToIdentifiedVo() }
        dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
//        hostNicVos { hostNics.fromHostNicsToIdentifiedVos() }
        hostNicVos { hostNics.toHostNicVos(conn) }
        vmVos { vms.fromVmsToIdentifiedVos() }
    }
}
fun List<Host>.toHostVos(conn: Connection): List<HostVo> =
    this@toHostVos.map { it.toHostVo(conn) }
