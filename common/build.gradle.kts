group = "com.itinfo.common"
description = "공통"
version = Versions.Project.ITCLOUD

val jar: Jar by tasks
jar.enabled = true
dependencies {
    compileOnly(Dependencies.kotlinStdlib)
    compileOnly(Dependencies.log4j)
    compileOnly(Dependencies.gson)
}