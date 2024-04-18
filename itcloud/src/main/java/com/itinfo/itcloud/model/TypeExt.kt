package com.itinfo.itcloud.model

import org.ovirt.engine.sdk4.types.*


// datacenter
fun QuotaModeType.findQuota() : String{
	return when(this){
		QuotaModeType.AUDIT -> "감사"
		QuotaModeType.DISABLED -> "비활성화됨"
		QuotaModeType.ENABLED -> "강제적용"
	}
}

fun DataCenterStatus.findDCStatus(): String {
	return when(this){
		DataCenterStatus.CONTEND ->"contend"
		DataCenterStatus.MAINTENANCE -> "maintenance"
		DataCenterStatus.NOT_OPERATIONAL -> "not_operational"
		DataCenterStatus.PROBLEMATIC -> "problematic"
		DataCenterStatus.UNINITIALIZED -> "초기화되지 않음"
		DataCenterStatus.UP -> "Up"
	}
}


// cluster
fun BiosType.findBios(): String {
	return when(this) {
		BiosType.I440FX_SEA_BIOS -> "BIOS의 1440FX 칩셋"
		BiosType.Q35_OVMF -> "UEFI의 Q35 칩셋"
		BiosType.Q35_SEA_BIOS -> "BIOS의 Q35 칩셋"
		BiosType.Q35_SECURE_BOOT -> "UEFI SecureBoot의 Q35 칩셋"
		else -> "자동 감지"
	}
}

fun MigrateOnError.findMigrateErr() : String{
	return when(this){
		MigrateOnError.DO_NOT_MIGRATE ->"아니요"
		MigrateOnError.MIGRATE ->"예"
		MigrateOnError.MIGRATE_HIGHLY_AVAILABLE ->"높은 우선 순위만"
	}
}

fun FipsMode.findFips(): String{
	return when(this) {
		FipsMode.UNDEFINED -> "자동 감지"
		FipsMode.DISABLED -> "비활성화됨"
		FipsMode.ENABLED -> "활성화됨"
	}
}

fun LogMaxMemoryUsedThresholdType.findLogMaxType(): String{
	return when(this){
		LogMaxMemoryUsedThresholdType.PERCENTAGE -> "%"
		LogMaxMemoryUsedThresholdType.ABSOLUTE_VALUE_IN_MB -> "MB"
	}
}

fun SwitchType.findSwitch(): String{
	return when(this){
		SwitchType.LEGACY -> "legacy"
		SwitchType.OVS -> "ovs(기술 어쩌구)"
	}
}


//fun NetworkUsage.findNwUsage() : String{
//	return when(this){
//		NetworkUsage.VM -> "vm"
//		NetworkUsage.GLUSTER -> "gluster"
//	}
//}



fun HostStatus.findHostStatus(): String {
	return when(this){
		HostStatus.UP -> ""
		HostStatus.DOWN -> ""
		HostStatus.ERROR -> ""
		HostStatus.MAINTENANCE -> ""
		HostStatus.UNASSIGNED -> ""
		HostStatus.NON_OPERATIONAL -> ""
		HostStatus.CONNECTING -> ""
		HostStatus.REBOOT -> ""
		HostStatus.KDUMPING -> ""
		HostStatus.INITIALIZING -> ""
		HostStatus.INSTALLING -> ""
		HostStatus.INSTALLING_OS -> ""
		HostStatus.INSTALL_FAILED -> ""
		HostStatus.NON_RESPONSIVE -> ""
		HostStatus.PENDING_APPROVAL -> ""
		HostStatus.PREPARING_FOR_MAINTENANCE -> ""
	}
}




fun LogSeverity.findLogSeverity() : String{
	return when(this){
		LogSeverity.ALERT -> "알림"
		LogSeverity.ERROR -> "에러"
		LogSeverity.NORMAL -> "보통"
		LogSeverity.WARNING -> "위험"
	}
}


fun NetworkStatus.findNetworkStatus() : String{
	return when(this){
		NetworkStatus.NON_OPERATIONAL -> "비가동 중"
		NetworkStatus.OPERATIONAL -> "가동 중"
	}
}


fun VmStatus.findVmStatus() : String{
	return when(this){
		VmStatus.UP -> "실행중"
		VmStatus.DOWN -> ""
		VmStatus.IMAGE_LOCKED -> ""
		VmStatus.MIGRATING -> ""
		VmStatus.NOT_RESPONDING -> ""
		VmStatus.PAUSED -> ""
		VmStatus.POWERING_DOWN -> ""
		VmStatus.POWERING_UP -> ""
		VmStatus.REBOOT_IN_PROGRESS -> ""
		VmStatus.RESTORING_STATE -> ""
		VmStatus.SAVING_STATE -> ""
		VmStatus.SUSPENDED -> ""
		VmStatus.UNASSIGNED -> ""
		VmStatus.UNKNOWN -> ""
		VmStatus.WAIT_FOR_LAUNCH -> ""
	}
}


fun VnicPassThroughMode.findVnicPass() : String{
	return when(this){
		VnicPassThroughMode.ENABLED -> "예"
		VnicPassThroughMode.DISABLED -> "아니요"
	}
}

fun VmType.findVmType(): String{
	return when(this){
		VmType.SERVER -> "서버"
		VmType.DESKTOP -> "데스크톱"
		VmType.HIGH_PERFORMANCE -> "고성능"
	}
}


fun DiskStorageType.findStorageType() : String{
	return when(this){
		DiskStorageType.MANAGED_BLOCK_STORAGE -> "Managed Block Storage"
		DiskStorageType.CINDER -> "Cinder"
		DiskStorageType.LUN -> "Lun"
		DiskStorageType.IMAGE -> "이미지"
	}
}













