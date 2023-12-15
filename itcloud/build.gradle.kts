import java.awt.Desktop
import java.net.URL

plugins {
    kotlin("jvm")
    application
    war
}

group = "com.itinfo.itcloud"
version = Versions.Project.ITCLOUD

val profile: String = if (project.hasProperty("profile")) project.property("profile") as? String ?: "local" else "local"
var artifactName: String = "itcloud-${profile}"
println("profile  : $profile")

val defaultBuildClassPath: String = "build/classes/kotlin/main"
val explodedWarName: String = "exploded"
val explodedWarPath: String = "$buildDir/libs/$explodedWarName"
val explodedWarDockerPath: String = "${project.rootDir}/docker/itcloud"
println("explodedWarPath  : $explodedWarPath")

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}

sourceSets {
    main {
        java.srcDirs(listOf("src/main/java", "src/main/kotlin"))
        resources.srcDirs(listOf("src/main/resources"))
        resources {
            srcDirs("src/main/resources", "src/main/resources-$profile")
        }
    }
    test {
        java.srcDirs(listOf("src/test/java"))
        resources.srcDirs(listOf("src/test/resources"))
    }
}

tasks.clean {
    delete(file("$explodedWarDockerPath/ROOT"))
}
tasks.compileJava { dependsOn(tasks.clean) }
tasks.compileKotlin {dependsOn(tasks.clean) }
// tasks.clean { finalizedBy(tasks.named("war")) }


dependencies {
    providedCompile(project(":common"))
    providedCompile(project(":util"))
    // tomcat(Dependencies.tomcatEmbedded)
    api(Dependencies.kotlinStdlib)
    implementation(Dependencies.ovirt)
    implementation(Dependencies.spring)
    implementation(Dependencies.springSecurity)
    implementation(Dependencies.swagger3)
    implementation(Dependencies.qemu)
    implementation(Dependencies.tiles)
    implementation(Dependencies.mybatis)
    implementation(Dependencies.log4j)
    providedCompile(Dependencies.javaxServlet)
    implementation(Dependencies.javaxServletJstl)
    implementation(Dependencies.javaxInject)
    implementation(Dependencies.javaxAnnotation)
    annotationProcessor(Dependencies.javaxAnnotation)
    implementation(Dependencies.webjars)
    runtimeOnly(Dependencies.jdbc)
    implementation(Dependencies.commons)
    implementation(Dependencies.jasypt)
    implementation(Dependencies.gson)
    implementation(Dependencies.jsonSimple)
    implementation(Dependencies.aspectj)
    implementation(Dependencies.cglib)
    compileOnly(Dependencies.lombok)
    annotationProcessor(Dependencies.lombok)
    testImplementation(Dependencies.springTest)
    testImplementation(Dependencies.hamcrest)
}

tasks.war {
    baseName = artifactName
    into("WEB-INF/classes") {
        from("../util/${defaultBuildClassPath}")
        from("../common/${defaultBuildClassPath}")
    }
    /*
    doFirst {
        copy {
            from("${project.rootDir}/util/${defaultBuildClassPath}")
            into("$buildDir/classes/kotlin/main")
        }
        copy {
            from("${project.rootDir}/common/${defaultBuildClassPath}")
            into("$buildDir/classes/kotlin/main")
        }
    }
    */
    finalizedBy(explodedWar)
}


val explodedWar by tasks.register<Copy>("explodedWar") {
    into(explodedWarPath)
    with(tasks.war.get())
}

val putModules = task("putModules") {
    doLast {
        // Smart Tomcat 을 위해 구성
        copy {
            from("${project.rootDir}/util/${defaultBuildClassPath}")
            into("$buildDir/classes/kotlin/main")
        }
        copy {
            from("${project.rootDir}/common/${defaultBuildClassPath}")
            into("$buildDir/classes/kotlin/main")
        }
        // 실제 exploded-war에 배치하도록 구성
        copy {
            from("${project.rootDir}/util/${defaultBuildClassPath}")
            into("$explodedWarPath/WEB-INF/classes")
        }
        copy {
            from("${project.rootDir}/common/${defaultBuildClassPath}")
            into("$explodedWarPath/WEB-INF/classes")
        }
    }
}

val placeOutputToDocker = task("placeOutputToDocker") {
    doLast {
        // delete(file("$explodedWarDockerPath/ROOT"))
        copy {
            from(explodedWarPath)
            into(file("${explodedWarDockerPath}/ROOT"))
        }
    }
}


explodedWar.finalizedBy(putModules)
putModules.finalizedBy(placeOutputToDocker)


task("openBrowser") {
    description = "open browser to the running application"
    doLast {
        val port: Int = 8080
        val contextName = "contextName"
        val url: URL = URL("http://localhost:$port/$contextName/")
        Desktop.getDesktop().browse(url.toURI())
    }
}

task("exploreOutput") {
    description = "find artifact(s) in the project directory"
    doLast {
        Desktop.getDesktop().open(layout.buildDirectory.dir("libs").get().asFile)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
