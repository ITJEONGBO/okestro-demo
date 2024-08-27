package com.itinfo.itcloud.error

import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.ItCloudException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ItemNotFoundException(
	message: String
): ItCloudException(message) {
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class IdNotFoundException(
	message: String
): ItCloudException(message) {
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidRequestException(
	message: String
): ItCloudException(message) {

}

fun ErrorPattern.toException(): ItCloudException {
	return when(this) {
		ErrorPattern.DATACENTER_ID_NOT_FOUND,
		ErrorPattern.CLUSTER_ID_NOT_FOUND,
		ErrorPattern.STORAGE_DOMAIN_ID_NOT_FOUND,
		ErrorPattern.HOST_ID_NOT_FOUND,
		ErrorPattern.DISK_ID_NOT_FOUND,
		ErrorPattern.DISK_IMAGE_ID_NOT_FOUND,
		ErrorPattern.DISK_ATTACHMENT_ID_NOT_FOUND,
		ErrorPattern.NETWORK_ID_NOT_FOUND,
		ErrorPattern.NIC_ID_NOT_FOUND,
		ErrorPattern.VM_ID_NOT_FOUND,
		ErrorPattern.VNIC_PROFILE_ID_NOT_FOUND,
		ErrorPattern.TEMPLATE_ID_NOT_FOUND,
		ErrorPattern.CONSOLE_ID_NOT_FOUND, -> IdNotFoundException("[${code}] ${term.desc} ${failureType.message}")
		ErrorPattern.DATACENTER_NOT_FOUND,
		ErrorPattern.CLUSTER_NOT_FOUND,
		ErrorPattern.STORAGE_DOMAIN_NOT_FOUND,
		ErrorPattern.HOST_NOT_FOUND,
		ErrorPattern.DISK_NOT_FOUND,
		ErrorPattern.DISK_IMAGE_NOT_FOUND,
		ErrorPattern.DISK_ATTACHMENT_NOT_FOUND,
		ErrorPattern.NETWORK_NOT_FOUND,
		ErrorPattern.NIC_NOT_FOUND,
		ErrorPattern.VM_NOT_FOUND,
		ErrorPattern.VNIC_PROFILE_NOT_FOUND,
		ErrorPattern.TEMPLATE_NOT_FOUND,
		ErrorPattern.CONSOLE_NOT_FOUND, -> ItemNotFoundException("[${code}] ${failureType.message} ${term.desc}")
		ErrorPattern.DATACENTER_VO_INVALID,
		ErrorPattern.CLUSTER_VO_INVALID,
		ErrorPattern.STORAGE_DOMAIN_VO_INVALID,
		ErrorPattern.HOST_VO_INVALID,
		ErrorPattern.DISK_VO_INVALID,
		ErrorPattern.DISK_IMAGE_VO_INVALID,
		ErrorPattern.DISK_ATTACHMENT_VO_INVALID,
		ErrorPattern.NETWORK_VO_INVALID,
		ErrorPattern.NIC_VO_INVALID,
		ErrorPattern.VM_VO_INVALID,
		ErrorPattern.VNIC_PROFILE_VO_INVALID,
		ErrorPattern.TEMPLATE_VO_INVALID,
		ErrorPattern.CONSOLE_VO_INVALID, -> InvalidRequestException("[${code}] ${term.desc} ${failureType.message}")
		else -> ItCloudException(failureType.message)
	}
}