package com.itinfo.itcloud.model.setting

import com.itinfo.itcloud.model.gson
import org.ovirt.engine.sdk4.builders.DomainBuilder
import org.ovirt.engine.sdk4.builders.UserBuilder
import org.ovirt.engine.sdk4.types.User
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(UserVo::class.java)
/**
 * [UserVo]
 * 
 * @property id [String]
 * @property name [String]
 * @property domain [String]
 * @property department [String]
 * @property email [String]
 * @property lastName [String]
 * @property nameSpace [String]
 * @property principal [String]
 * @property userName [String]
 * @property provider [String]
 * @property roleVo [RoleVo]
 * link - groups, roles, permissions, tags, sshpublickeys, eventsubscriptions, options
 * domain
 */
class UserVo(
	val id: String = "",
	val name: String = "",
	val domain: String = "internal-authz",
	val password: String = "",  // 보안
	val department: String = "",
	val email: String = "",
	val lastName: String = "",
	val nameSpace: String = "",
	val principal: String = "",
	val userName: String = "",
	val provider: String = "",
	val roleVo: RoleVo = RoleVo(),
): Serializable {
	override fun toString(): String =
		gson.toJson(this)

	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDomain: String = "internal-authz";fun domain(block: () -> String?) { bDomain = block() ?: "internal-authz" }
		private var bPassword: String = "";fun password(block: () -> String?) { bPassword = block() ?: "" }
		private var bDepartment: String = "";fun department(block: () -> String?) { bDepartment = block() ?: "" }
		private var bEmail: String = "";fun email(block: () -> String?) { bEmail = block() ?: "" }
		private var bLastName: String = "";fun lastName(block: () -> String?) { bLastName = block() ?: "" }
		private var bNameSpace: String = "";fun nameSpace(block: () -> String?) { bNameSpace = block() ?: "" }
		private var bPrincipal: String = "";fun principal(block: () -> String?) { bPrincipal = block() ?: "" }
		private var bUserName: String = "";fun userName(block: () -> String?) { bUserName = block() ?: "" }
		private var bProvider: String = "";fun provider(block: () -> String?) { bProvider = block() ?: "" }
		private var bRoleVo: RoleVo = RoleVo();fun roleVo(block: () -> RoleVo?) { bRoleVo = block() ?: RoleVo() }
		fun build(): UserVo = UserVo(bId, bName, bDomain, bPassword, bDepartment, bEmail, bLastName, bNameSpace, bPrincipal, bUserName, bProvider, bRoleVo)
	}

	companion object {
		inline fun builder(block: UserVo.Builder.() -> Unit): UserVo = UserVo.Builder().apply(block).build()
	}
}

fun UserVo.toUserBuilder(): UserBuilder {
	return UserBuilder()
		.userName(this@toUserBuilder.userName +"@"+ this@toUserBuilder.domain)
		.domain(DomainBuilder().name(this@toUserBuilder.domain).build())
	// TODO role이 들어가던지 ..
}

fun UserVo.toAddUserBuilder(): User =
	this@toAddUserBuilder.toUserBuilder().build()

fun UserVo.toEditUserBuilder(): User =
	this@toEditUserBuilder.toUserBuilder().id(this@toEditUserBuilder.id).build()

fun User.toUserVo(): UserVo{
	return UserVo.builder {
		id { this@toUserVo.id() }
		name { this@toUserVo.name() }
		userName { this@toUserVo.userName() }
		domain { this@toUserVo.domain().name() }//?
	}
}