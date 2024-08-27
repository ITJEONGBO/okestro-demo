package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.UserService
import org.ovirt.engine.sdk4.services.UsersService
import org.ovirt.engine.sdk4.types.User

private fun Connection.srvUsers(): UsersService =
	systemService().usersService()

fun Connection.findAllUsers(follow: String = ""): Result<List<User>> = runCatching {
	if (follow.isNotEmpty())
		this.srvUsers().list().follow(follow).send().users()
	else
		this.srvUsers().list().send().users()
}.onSuccess {
	Term.USER.logSuccess("목록조회")
}.onFailure {
	Term.USER.logFail("목록조회", it)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvUser(userId: String): UserService =
	this.srvUsers().userService(userId)

fun Connection.findUser(userId: String): Result<User?> = runCatching {
	this.srvUser(userId).get().send().user()
}.onSuccess {
	Term.USER.logSuccess("상세조회")
}.onFailure {
	Term.USER.logFail("상세조회", it)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.addUser(user: User): Result<User?> = runCatching {
	this.srvUsers().add().user(user).send().user()
}.onSuccess {
	Term.USER.logSuccess("생성")
}.onFailure {
	Term.USER.logFail("생성", it)
	throw if (it is Error) it.toItCloudException() else it
}