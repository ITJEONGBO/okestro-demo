group = "com.itinfo.util.ovirt"
description = "유틸 (ovirt)"
version = Versions.Project.ITCLOUD

val jar: Jar by tasks
jar.enabled = true
dependencies {
    compileOnly(Dependencies.kotlinStdlib)
    compileOnly(Dependencies.log4j)
    compileOnly(Dependencies.gson)
    compileOnly(Dependencies.ovirt)

    compileOnly("com.jcraft:jsch:0.1.55")
}