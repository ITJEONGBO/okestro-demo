group = "com.itinfo.itcloud.license"
description = "라이센스 공통"
version = Versions.Project.ITCLOUD

val jar: Jar by tasks
jar.enabled = true

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":util"))
    compileOnly(Dependencies.kotlinStdlib)
    compileOnly(Dependencies.log4j)
    compileOnly(Dependencies.gson)

    testImplementation(project(":common"))
    testImplementation(project(":util"))
    testImplementation(Dependencies.log4j)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.hamcrest)
}