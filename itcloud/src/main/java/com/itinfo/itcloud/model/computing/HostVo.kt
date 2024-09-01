package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.network.toHostNicVos
import com.itinfo.itcloud.ovirtDf
import com.itinfo.itcloud.repository.dto.UsageDto
import com.itinfo.util.ovirt.*
import org.slf4j.LoggerFactory
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.*
import java.io.Serializable
import java.math.BigInteger
import java.util.Date

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
 * @property ksm [Boolean] 메모리 페이지 공유
 * @property seLinux [SeLinuxMode] SeLinuxMode(disabled, enforcing, permissive)
 * @property hostedEngine [Boolean] spm Hosted Engine HA [ 금장, 은장, null ]
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
 * @property vmMigratingCnt [Int] summary
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
// * @property usageDto [UsageDto]
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
    val spmPriority: Int = 0,
    val spmStatus: SpmStatus = SpmStatus.NONE,
    val sshFingerPrint: String = "",
    val sshPort: Int = 0,
    val sshPublicKey: String = "",
    val sshName: String = "",
    val sshPassWord: String = "",
    val status: HostStatus = HostStatus.NON_RESPONSIVE,
    val transparentPage: Boolean = false,
    val vmTotalCnt: Int = 0,
    val vmActiveCnt: Int = 0,
    val vmMigratingCnt: Int = 0,
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
//    val hostNicVos: List<IdentifiedVo> = listOf(),
    val vmVos: List<IdentifiedVo> = listOf()
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
        private var bSpmPriority: Int = 0; fun spmPriority(block: () -> Int?) { bSpmPriority = block() ?: 0 }
        private var bSpmStatus: SpmStatus = SpmStatus.NONE; fun spmStatus(block: () -> SpmStatus?) { bSpmStatus = block() ?: SpmStatus.NONE }
        private var bSshFingerPrint: String = ""; fun sshFingerPrint(block: () -> String?) { bSshFingerPrint = block() ?: "" }
        private var bSshPort: Int = 0; fun sshPort(block: () -> Int?) { bSshPort = block() ?: 0 }
        private var bSshPublicKey: String = ""; fun sshPublicKey(block: () -> String?) { bSshPublicKey = block() ?: "" }
        private var bSshName: String = ""; fun sshName(block: () -> String?) { bSshName = block() ?: "" }
        private var bSshPassWord: String = ""; fun sshPassWord(block: () -> String?) { bSshPassWord = block() ?: "" }
        private var bStatus: HostStatus = HostStatus.NON_RESPONSIVE; fun status(block: () -> HostStatus?) { bStatus = block() ?: HostStatus.NON_RESPONSIVE }
        private var bTransparentPage: Boolean = false; fun transparentPage(block: () -> Boolean?) { bTransparentPage = block() ?: false }
        private var bVmTotalCnt: Int = 0; fun vmTotalCnt(block: () -> Int?) { bVmTotalCnt = block() ?: 0 }
        private var bVmActiveCnt: Int = 0; fun vmActiveCnt(block: () -> Int?) { bVmActiveCnt = block() ?: 0 }
        private var bVmMigratingCnt: Int = 0; fun vmMigratingCnt(block: () -> Int?) { bVmMigratingCnt = block() ?: 0 }
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
//        private var bHostNicVos: List<IdentifiedVo> = listOf(); fun hostNicVos(block: () -> List<IdentifiedVo>?) { bHostNicVos = block() ?: listOf() }
        private var bHostNicVos: List<HostNicVo> = listOf(); fun hostNicVos(block: () -> List<HostNicVo>?) { bHostNicVos = block() ?: listOf() }
        private var bVmVos: List<IdentifiedVo> = listOf(); fun vmVos(block: () -> List<IdentifiedVo>?) { bVmVos = block() ?: listOf() }

        fun build(): HostVo = HostVo(bId, bName, bComment, bAddress, bDevicePassThrough, bHostedActive, bHostedScore, bIscsi, bKdump, bKsm, bSeLinux, bHostedEngine, bSpmPriority, bSpmStatus, bSshFingerPrint, bSshPort, bSshPublicKey, bSshName, bSshPassWord, bStatus, bTransparentPage, bVmTotalCnt, bVmActiveCnt, bVmMigratingCnt, bMemoryTotal, bMemoryUsed, bMemoryFree, bMemoryMax, bMemoryShared, bSwapTotal, bSwapUsed, bSwapFree, bHugePage2048Free, bHugePage2048Total, bHugePage1048576Free, bHugePage1048576Total, bBootingTime, bHostHwVo, bHostSwVo, bClusterVo, bDataCenterVo, bHostNicVos, bVmVos)
    }
    companion object {
        inline fun builder(block: HostVo.Builder.() -> Unit): HostVo = HostVo.Builder().apply(block).build()
    }
}


fun Host.toHostIdName(): HostVo = HostVo.builder {
    id { this@toHostIdName.id() }
    name { this@toHostIdName.name() }
}

fun List<Host>.toHostIdNames(): List<HostVo> =
    this@toHostIdNames.map { it.toHostIdName() }



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
        vmTotalCnt { this@toHostVo.summary().totalAsInteger() }
        vmActiveCnt { this@toHostVo.summary().activeAsInteger() }
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


fun HostVo.toHostBuilder(): HostBuilder {
    return HostBuilder()
        .cluster(ClusterBuilder().id(this@toHostBuilder.clusterVo.id))
        .name(this@toHostBuilder.name)
        .comment(this@toHostBuilder.comment)
        .address(this@toHostBuilder.address)
        .ssh(SshBuilder().port(this@toHostBuilder.sshPort)) // 기본값이 22 포트 연결은 더 테스트 해봐야함(ovirt 내에서 한적은 없음)
        .rootPassword(this@toHostBuilder.sshPassWord)
        .powerManagement(PowerManagementBuilder().enabled(false)) // 전원관리 비활성화 (기본)
        .spm(SpmBuilder().priority(this@toHostBuilder.spmPriority))
        /*.hostedEngine(
            HostedEngineBuilder()
                // .active() // 호스트 엔진 배치 작업(없음, 배포)
                .build()
        )*/
}

fun HostVo.toAddHostBuilder(): Host =
    this@toAddHostBuilder.toHostBuilder().build()

fun HostVo.toEditHostBuilder(): Host =
    this@toEditHostBuilder.toHostBuilder().id(this@toEditHostBuilder.id).build()

