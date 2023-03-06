import com.bmuschko.gradle.tomcat.extension.TomcatPluginExtension

plugins {
    kotlin("jvm")
    application
    war
    id("com.bmuschko.tomcat") version Versions.tomcatPlugin
}

group = "com.itinfo"
version = "0.0.1"

sourceSets {
    main {
        java.srcDirs(listOf("src/main/java"))
        resources.srcDirs(listOf("src/main/resources"))
    }
    test {
        java.srcDirs(listOf("src/test/java"))
        resources.srcDirs(listOf("src/test/resources"))
    }
}

tomcat {
    httpPort = 8089
    httpsPort = 8079
    ajpPort = 8019
    /*
    jasper {
        validateTld = true
        validateXml = true
    }
    */
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    tomcat(Dependencies.tomcatEmbedded)
    implementation(Dependencies.kotlinStdlib)
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

application {
    mainClass.set("MainKt")
}
