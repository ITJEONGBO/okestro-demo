package com.itinfo.itcloud.model.enums;

public enum ChipsetVo {
    cluster_default("자동 감지"),
    i440fx_sea_bios("BIOS의 1440FX 칩셋"),
    q35_ovmf("UEFI의 Q35 칩셋"),
    q35_sea_bios("BIOS의 Q35 칩셋"),
    q35_secure_boot("UEFI SecureBoot의 Q35 칩셋");

    public final String name;
    ChipsetVo(String name) {
        this.name = name;
    }

}
