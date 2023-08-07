import java.awt.Desktop
import java.net.URL

plugins {
    kotlin("jvm")
    application
    id("com.bmuschko.tomcat") version Versions.tomcatPlugin
}

group = "com.itinfo"
version = Versions.Project.OKESTRO

val profile: String = if (project.hasProperty("profile")) project.property("profile") as? String ?: "local" else "local"
var artifactName: String = "okestro-monolith-${profile}"
println("profile  : $profile")
val defaultBuildClassPath: String = "build/classes/kotlin/main"
val explodedWarName: String = "$artifactName-exploded"
val explodedWarPath: String = "$buildDir/libs/$explodedWarName"
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
            srcDirs("src/main/resources-local", "src/main/resources-$profile")
        }
    }
    test {
        java.srcDirs(listOf("src/test/java"))
        resources.srcDirs(listOf("src/test/resources"))
    }
}
tasks.compileJava { dependsOn(tasks.clean) }
tasks.compileKotlin {dependsOn(tasks.clean) }
// tasks.clean { finalizedBy(tasks.named("war")) }


dependencies {
    providedCompile(project(":common"))
    providedCompile(project(":util"))
    tomcat(Dependencies.tomcatEmbedded)
    api(Dependencies.kotlinStdlib)
    implementation(Dependencies.ovirt)
    implementation(Dependencies.spring)
    implementation(Dependencies.springSecurity)
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
    testImplementation(Dependencies.hamcrest)
}


tasks.war {
    // webXml = file("src/main/webapp/WEB-INF/web.xml")
    baseName = artifactName
    finalizedBy(explodedWar)
//    into("$explodedWarPath/WEB-INF/classes") {
//        from("${project.rootDir}/util/${defaultBuildClassPath}")
//        from("${project.rootDir}/common/${defaultBuildClassPath}")
//    }
}

val explodedWar by tasks.register<Copy>("explodedWar") {
    into(explodedWarPath)
    with(tasks.war.get())
}
val putModules = task("putModules") {
    doLast {
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

val placeOutputToDocker by tasks.register<Copy>("placeOutputToDocker") {
    println("execute placeOutputToDocker!")
    from(explodedWarPath)
    into(file("${project.rootDir}/docker/okestro/$explodedWarName"))
    include(explodedWarName)
    rename(explodedWarName, "ROOT")
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


explodedWar.finalizedBy(placeOutputToDocker)


tomcat {
    httpProtocol = "org.apache.coyote.http11.Http11Nio2Protocol"
    ajpProtocol  = "org.apache.coyote.ajp.AjpNio2Protocol"
    httpPort = 8089
    httpsPort = 8079
    ajpPort = 8019
    enableSSL = false
    contextPath = ""
    /*
    jasper {
        validateTld = true
        validateXml = true
    }
    */
}

tasks.tomcatRunWar {
    configFile = file("src/main/webapp/META-INF/context.xml")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
