package com.itinfo.util.ovirt

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.services.PermissionService
import org.ovirt.engine.sdk4.services.SystemPermissionsService
import org.ovirt.engine.sdk4.types.Permission

private fun Connection.srvSystemPermissions(): SystemPermissionsService =
	systemService.permissionsService()

fun Connection.findAllSystemPermissions(follow: String): Result<List<Permission>> = runCatching {
	if (follow.isNotEmpty())
		this.srvSystemPermissions().list().follow(follow).send().permissions()
	else
		this.srvSystemPermissions().list().send().permissions()
}.onSuccess {
	Term.SYSTEM_PERMISSION.logSuccess("목록조회")
}.onFailure {
	Term.SYSTEM_PERMISSION.logFail("목록조회", it)
	throw it
}

fun Connection.srvSystemPermission(permissionId: String): PermissionService =
	this.srvSystemPermissions().permissionService(permissionId)

fun Connection.findSystemPermission(permissionId: String): Result<Permission?> = runCatching {
	this.srvSystemPermission(permissionId).get().send().permission()
}.onSuccess {
	Term.SYSTEM_PERMISSION.logSuccess("상세조회", permissionId)
}.onFailure {
	Term.SYSTEM_PERMISSION.logFail("상세조회", it, permissionId)
	throw it
}

fun Connection.addSystemPermission(permission: Permission): Result<Permission?> = runCatching {
	this.srvSystemPermissions().add().permission(permission).send().permission()
}.onSuccess {
	Term.SYSTEM_PERMISSION.logSuccess("생성", it.id())
}.onFailure {
	Term.SYSTEM_PERMISSION.logFail("생성", it)
	throw it
}

fun Connection.removePermission(permissionId: String): Result<Boolean> = runCatching {
	this.srvSystemPermission(permissionId).remove().send()
	true
}.onSuccess {
	Term.SYSTEM_PERMISSION.logSuccess("삭제", permissionId)
}.onFailure {
	Term.SYSTEM_PERMISSION.logFail("삭제", it, permissionId)
	throw it
}