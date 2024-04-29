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

// /ovirt-engine/api/operatingsystems OperatingSystemInfo

fun OsVo.findOs() : String{
	return when(this){
		OsVo.other -> "Other OS"
		OsVo.windows_xp -> "Windows XP"
		OsVo.windows_2003 -> "Windows 2003"
		OsVo.windows_2008 -> "Windows 2008"
		OsVo.other_linux -> "Linux"
		OsVo.rhel_5 -> "Red Hat Enterprise Linux 5.x"
		OsVo.rhel_4 -> "Red Hat Enterprise Linux 4.x"
		OsVo.rhel_3 -> "Red Hat Enterprise Linux 3.x"
		OsVo.windows_2003x64 -> "Windows 2003 x64"
		OsVo.windows_7 -> "Windows 7"
		OsVo.windows_7x64 -> "Windows 7 x64"
		OsVo.rhel_5x64 -> "Red Hat Enterprise Linux 5.x x64"
		OsVo.rhel_4x64 -> "Red Hat Enterprise Linux 4.x x64"
		OsVo.rhel_3x64 -> "Red Hat Enterprise Linux 3.x x64"
		OsVo.windows_2008x64 -> "Windows 2008 x64"
		OsVo.windows_2008R2x64 -> "Windows 2008 R2 x64"
		OsVo.rhel_6 -> "Red Hat Enterprise Linux 6.x"
		OsVo.rhel_6x64 -> "Red Hat Enterprise Linux 6.x x64"
		OsVo.debian_7 -> "Debian 7+"
		OsVo.windows_8 -> "Windows 8"
		OsVo.debian_9 -> "Debian 9+"
		OsVo.windows_8x64 -> "Windows 8 x64"
		OsVo.windows_2012x64 -> "Windows 2012 x64"
		OsVo.rhel_7x64 -> "Red Hat Enterprise Linux 7.x x64"
		OsVo.windows_2012R2x64 -> "Windows 2012R2 x64"
		OsVo.windows_10 -> "Windows 10"
		OsVo.windows_10x64 -> "Windows 10 x64"
		OsVo.rhel_atomic7x64 -> "Red Hat Atomic 7.x x64"
		OsVo.windows_2016x64 -> "Windows 2016 x64"
		OsVo.rhel_8x64 -> "Red Hat Enterprise Linux 8.x x64"
		OsVo.windows_2019x64 -> "Windows 2019 x64"
		OsVo.other_linux_kernel_4 -> "Other Linux (kernel 4.x)"
		OsVo.rhel_9x64 -> "Red Hat Enterprise Linux 9.x x64"
		OsVo.rhcos_x64 -> "Red Hat Enterprise Linux CoreOS"
		OsVo.windows_11 -> "Windows 11"
		OsVo.windows_2022 -> "Windows 2022"
		OsVo.sles_11 -> "SUSE Linux Enterprise Server 11+"
		OsVo.other_s390x -> "Other OS"
		OsVo.other_linux_s390x -> "Linux"
		OsVo.rhel_7_s390x -> "Red Hat Enterprise Linux 7.x"
		OsVo.sles_12_s390x -> "SUSE Linux Enterprise Server 12"
		OsVo.ubuntu_16_04_s390x -> "Ubuntu Xenial Xerus LTS+"
		OsVo.freebsd -> "FreeBSD 9.2"
		OsVo.freebsdx64 -> "FreeBSD 9.2 x64"
		OsVo.ubuntu_12_04 -> "Ubuntu Precise Pangolin LTS"
		OsVo.ubuntu_12_10 -> "Ubuntu Quantal Quetzal"
		OsVo.ubuntu_13_04 -> "Ubuntu Raring Ringtails"
		OsVo.ubuntu_13_10 -> "Ubuntu Saucy Salamander"
		OsVo.ubuntu_14_04 -> "Ubuntu Trusty Tahr LTS+"
		OsVo.other_ppc64 -> "Other OS"
		OsVo.ubuntu_18_04 -> "Ubuntu Bionic Beaver LTS+"
		OsVo.other_linux_ppc64 -> "Linux"
		OsVo.rhel_6_ppc64 -> "Red Hat Enterprise Linux up to 6.8"
		OsVo.sles_11_ppc64 -> "SUSE Linux Enterprise Server 11"
		OsVo.ubuntu_14_04_ppc64 -> "Ubuntu Trusty Tahr LTS+"
		OsVo.rhel_7_ppc64 -> "Red Hat Enterprise Linux 7.x"
		OsVo.rhel_6_9_plus_ppc64 -> "Red Hat Enterprise Linux 6.9+"
		OsVo.rhel_8_ppc64 -> "Red Hat Enterprise Linux 8.x"
		OsVo.rhel_9_ppc64 -> "Red Hat Enterprise Linux 9.x"
	}
}










