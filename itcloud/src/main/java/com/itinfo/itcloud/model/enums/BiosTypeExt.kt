package com.itinfo.itcloud.model.enums

import org.ovirt.engine.sdk4.types.BiosType

fun BiosType.findKRName(): String {
	return when(this) {
		BiosType.I440FX_SEA_BIOS -> "BIOS의 1440FX 칩셋"
		BiosType.Q35_OVMF -> "UEFI의 Q35 칩셋"
		BiosType.Q35_SEA_BIOS -> "BIOS의 Q35 칩셋"
		BiosType.Q35_SECURE_BOOT -> "UEFI SecureBoot의 Q35 칩셋"
		else -> "자동 감지"
	}
}

