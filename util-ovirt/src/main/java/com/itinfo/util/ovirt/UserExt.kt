package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.UserService
import org.ovirt.engine.sdk4.services.UsersService
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.HostStatus
import org.ovirt.engine.sdk4.types.User
import java.net.InetAddress

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

fun Connection.changeUserPw(hostIp: String, hostPw: String, username: String, newPw: String): Result<Boolean> = runCatching {
	val address: InetAddress = InetAddress.getByName(hostIp)
	if (address.changePwHostViaSSH("root", hostPw, 22, username, newPw).isFailure)
		return Result.failure(Error("SSH 실패"))
	true
}.onSuccess {
	Term.USER.logSuccess("비밀번호 변경 성공")
}.onFailure {
	Term.USER.logFail("비밀번호 변경 실패", it)
	throw if (it is Error) it.toItCloudException() else it
}

/**
 * [InetAddress.changePwHostViaSSH]
 * host SSH 관리 - 비밀번호 변경
 * TODO 비번 보안문제?
 */
fun InetAddress.changePwHostViaSSH(hostname: String, password: String, port: Int, username: String, newPw: String): Result<Boolean> = runCatching {
	log.debug("ssh 시작")
	// SSH 세션 생성 및 연결
	val session: com.jcraft.jsch.Session = JSch().getSession(hostname, hostAddress, port)
	session.setPassword(password)
	session.setConfig("StrictHostKeyChecking", "no") // 호스트 키 확인을 건너뛰기 위해 설정
	session.connect()

	val channel: ChannelExec = session.openChannel("exec") as ChannelExec // SSH 채널 열기
//	val command = "echo -e \"$newPw\n$newPw\" | ovirt-aaa-jdbc-tool user password-reset $username"
//	channel.setCommand(command)
	channel.setCommand("ovirt-aaa-jdbc-tool user password-reset $username")
	Thread.sleep(1000)
	channel.setCommand(newPw)
	Thread.sleep(1000)
	channel.setCommand(newPw)
	Thread.sleep(1000)

	channel.connect()

	// 명령 실행 완료 대기
	while (!channel.isClosed) {
		Thread.sleep(100)
	}

	channel.disconnect()
	session.disconnect()
	val exitStatus = channel.exitStatus
	log.debug("changePwHostViaSSH 완료, exit status: $exitStatus")
	return Result.success(exitStatus == 0)
}.onSuccess {

}.onFailure {
	log.error(it.localizedMessage)
	throw if (it is Error) it.toItCloudException() else it
}
