group = "com.itinfo.itcloud.license"
description = "라이센스 값검증"
version = Versions.Project.ITCLOUD

val jar: Jar by tasks
jar.enabled = true

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":license-common"))
    compileOnly(project(":license-enc"))
    compileOnly(project(":license-dec"))
    compileOnly(Dependencies.kotlinStdlib)
    compileOnly(Dependencies.log4j)
    compileOnly(Dependencies.gson)

    testImplementation(project(":common"))
    testImplementation(Dependencies.log4j)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.hamcrest)
}