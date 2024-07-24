package com.itinfo.itcloud.model.setting

import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.SystemService
import org.ovirt.engine.sdk4.types.Group
import org.ovirt.engine.sdk4.types.Permission
import org.ovirt.engine.sdk4.types.Role
import org.ovirt.engine.sdk4.types.User
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.util.*

private val log = LoggerFactory.getLogger(PermissionVo::class.java)
/**
 * [PermissionVo]
 * 권한
 *
 * @property id [String]
 * @property user [String]
 * @property nameSpace [String]
 * @property role [String]
 * @property createDate [Date]
 * @property provider [String]
 * @property inheritedFrom [String]
 */
class PermissionVo (
    val id: String = "" ,
    val user: String = "" ,
    val nameSpace: String = "" ,
    val role: String = "" ,
    val createDate: Date = Date(),
    val provider: String = "" ,
    val inheritedFrom: String = ""
):Serializable{
    override fun toString(): String =
        gson.toJson(this)

    class Builder{
        private var bid: String = ""; fun id(block: () -> String?) {bid = block() ?: ""}
        private var buser: String = ""; fun user(block: () -> String?) {buser = block() ?: ""}
        private var bnameSpace: String = ""; fun nameSpace(block: () -> String?) {bnameSpace = block() ?: ""}
        private var brole: String = ""; fun role(block: () -> String?) {brole = block() ?: ""}
        private var bcreateDate: Date = Date(); fun createDate(block: () -> Date?) {bcreateDate = block() ?: Date()}
        private var bprovider: String = ""; fun provider(block: () -> String?) {bprovider = block() ?: ""}
        private var binheritedFrom: String = ""; fun inheritedFrom(block: () -> String?) {binheritedFrom = block() ?: ""}
        fun build(): PermissionVo = PermissionVo(bid, buser, bnameSpace, brole, bcreateDate, bprovider, binheritedFrom)
    }

    companion object {
        inline fun builder(block: PermissionVo.Builder.() -> Unit): PermissionVo = PermissionVo.Builder().apply(block).build()
    }
}

fun Permission.toPermissionVo(conn: Connection): PermissionVo? {
    log.debug("Permission.toPermissionVo ... ")
    val role: Role? =
        conn.findRole(this@toPermissionVo.role().id())
            .getOrNull()
    if (this@toPermissionVo.groupPresent() && !this@toPermissionVo.userPresent()) {
        val group: Group? =
            conn.findGroup(this@toPermissionVo.group().id())
                .getOrNull()
        return PermissionVo.builder {
            id { this@toPermissionVo.id() }
            user { group?.name() }
            nameSpace { group?.namespace() }
            role { role?.name() }
        }
    }

    if (!this@toPermissionVo.groupPresent() && this@toPermissionVo.userPresent()) {
        val user: User? =
            conn.findUser(this@toPermissionVo.user().id())
                .getOrNull()
        return PermissionVo.builder {
            id { this@toPermissionVo.id() }
            user { user?.name() }
            provider { user?.domain()?.name() }
            nameSpace { user?.namespace() }
            role { role?.name() }
        }
    }
    return null
}

// 권한
fun List<Permission>.toPermissionVos(conn: Connection): List<PermissionVo> =
    this@toPermissionVos.mapNotNull { it.toPermissionVo(conn) }