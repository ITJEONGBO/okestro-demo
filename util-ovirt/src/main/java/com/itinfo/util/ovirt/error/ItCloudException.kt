package com.itinfo.util.ovirt.error

open class ItCloudException(
	message: String = "IT클라우드 예외 발생",
): RuntimeException(message) {
}