package com.itinfo.dao.aaa

import com.itinfo.dao.gson
import com.itinfo.model.UserVo

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * [OvirtUser]
 * engine 엔티티: USERS
 *
 * USER-SQL > USER
 * @see com.itinfo.model.UserVo
 */
@Entity
@Table(name = "users", schema = "aaa_jdbc")
class OvirtUser(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id:	Int,

	val uuid: String,
	val name: String,
	val password: String,
	val passwordValidTo: LocalDateTime,
	val loginAllowed: String,
	val nopasswd: Int,
	val disabled: Int,
	val unlockTime: LocalDateTime,
	val lastSuccessfulLogin: LocalDateTime,
	val lastUnsuccessfulLogin: LocalDateTime,
	val consecutiveFailures: Int,
	val validFrom: LocalDateTime,
	val validTo: LocalDateTime,
): Serializable {
	override fun toString(): String = gson.toJson(this)
}

fun OvirtUser.toUserVo(): UserVo = UserVo.userVo {
	id { this@toUserVo.name }
	password { this@toUserVo.password }
	// name { this@toUserVo.name }
	// lastName { this@toUserVo.surname }
	// email { this@toUserVo.email }
	// principal { this@toUserVo.namespace }
	// administrative { this@toUserVo.lastAdminCheckStatus }
}

fun List<OvirtUser>.toUserVos(): List<UserVo> =
	this.map { it.toUserVo() }
