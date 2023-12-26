plugins {
    kotlin("jvm")
}

group = "com.itinfo.util"
version = Versions.Project.OKESTRO

sourceSets {
    main {
        java.srcDirs(listOf("src/main/java", "src/main/kotlin"))
        resources.srcDirs(listOf("src/main/resources"))
    }
    test {
        java.srcDirs(listOf("src/test/java", "src/test/kotlin"))
        resources.srcDirs(listOf("src/test/resources"))
    }
}
tasks.compileJava { dependsOn(tasks.clean) }
tasks.compileKotlin {dependsOn(tasks.clean) }

dependencies {
    compileOnly(Dependencies.kotlinStdlib)
    compileOnly(Dependencies.log4j)
    compileOnly(Dependencies.gson)
}