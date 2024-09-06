import org.springframework.boot.gradle.tasks.run.BootRun
import java.awt.Desktop
import java.net.URL
import java.util.Locale

plugins {
    war
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.jetbrains.kotlin.plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

springBoot {
    group = "com.itinfo.itcloud"
    description = "아이티클라우드 백엔드"
    version = Versions.Project.ITCLOUD
}

val profile: String = project.findProperty("profile") as? String ?: "local"
val skipNpm: Boolean = (project.findProperty("skipNpm") as? String)?.toBoolean() ?: false
var artifactName: String = "itcloud-${profile}"
println("profile  : $profile")
println("skipNpm  : $skipNpm")
val defaultBuildClassPath: String = "build/classes/kotlin/main"
val explodedWarName: String = "exploded"
val explodedWarPath: String = "$buildDir/libs/$explodedWarName"
val explodedWarDockerPath: String = "${project.rootDir}/docker/itcloud"
println("explodedWarPath  : $explodedWarPath")

tasks.clean {
    delete(file("$explodedWarDockerPath/ROOT"))
}

dependencies {
    implementation(project(":common"))
    implementation(project(":util"))
    implementation(project(":util-ovirt"))
    implementation(Dependencies.springBootTomcat)
    implementation(Dependencies.tomcatEmbedded)
    api(Dependencies.kotlinStdlib)
    implementation(Dependencies.ovirt)
    implementation(Dependencies.springBoot)
    implementation(Dependencies.springBootWeb) {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
    annotationProcessor(Dependencies.springBootAnnotation)
    developmentOnly(Dependencies.springBootDevtools)
    implementation(Dependencies.spring)
    implementation(Dependencies.springSecurity)
    implementation(Dependencies.jwt)
    runtimeOnly(Dependencies.jwtRuntime)
    implementation(Dependencies.swagger3)
    implementation(Dependencies.qemu)
    implementation(Dependencies.tiles)
    implementation(Dependencies.mybatis)
    // implementation(Dependencies.log4j)
    providedCompile(Dependencies.javaxServlet)
    implementation(Dependencies.javaxServletJstl)
    implementation(Dependencies.javaxInject)
    implementation(Dependencies.javaxAnnotation)
    annotationProcessor(Dependencies.javaxAnnotation)
    implementation(Dependencies.webjars)
    implementation(Dependencies.jdbc)
    implementation(Dependencies.commons)
    implementation(Dependencies.jasypt)
    implementation(Dependencies.gson)
    implementation(Dependencies.jsonSimple)
    implementation(Dependencies.aspectj)
    implementation(Dependencies.cglib)
    compileOnly(Dependencies.lombok)
    annotationProcessor(Dependencies.lombok)
    testImplementation(Dependencies.springTest)
    testImplementation(Dependencies.springBootTest)
    testImplementation(Dependencies.hamcrest)
    implementation("com.jcraft:jsch:0.1.55")
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")

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

val openBrowser = task("openBrowser") {
    description = "open browser to the running application"
    doLast {
        val port = 8443
        val contextName = "swagger-ui/"
        val url: URL = URL("https://localhost:$port/$contextName")
        Desktop.getDesktop().browse(url.toURI())
    }
}

tasks.named<BootRun>("bootRun") {
    dependsOn(openBrowser)
}

task("exploreOutput") {
    description = "find artifact(s) in the project directory"
    doLast {
        Desktop.getDesktop().open(layout.buildDirectory.dir("libs").get().asFile)
    }
}

// react 프로젝트 구성
val frontendDir = "src/main/frontend"

tasks.processResources {
    if (!skipNpm) {
        dependsOn("copyReactBuildFiles")
    }
}

val installReact by tasks.register<Exec>("installReact") {
    workingDir(frontendDir)
    inputs.dir(frontendDir)
    group = BasePlugin.BUILD_GROUP
    if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
        commandLine("npm.cmd", "audit", "fix")
        commandLine("npm.cmd", "install")
    } else {
        commandLine("npm", "audit", "fix")
        commandLine("npm", "install")
    }
}

val buildReact by tasks.register<Exec>( "buildReact") {
    dependsOn("installReact")
    workingDir(frontendDir)
    inputs.dir(frontendDir)
    group = BasePlugin.BUILD_GROUP
    if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
        commandLine("npm.cmd", "run-script", "build")
    } else {
        commandLine("npm", "run-script", "build")
    }
}

val copyReactBuildFiles by tasks.register<Copy>("copyReactBuildFiles") {
    dependsOn("buildReact")
    from("$frontendDir/build")
    into("$projectDir/src/main/resources/static")
}