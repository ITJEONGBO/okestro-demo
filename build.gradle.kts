import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version Versions.kotlin
    kotlin("plugin.spring") version Versions.kotlin
    kotlin("plugin.jpa") version Versions.kotlin
    id("org.springframework.boot") version Versions.springBoot
    id("org.jetbrains.dokka") version Versions.kotlin
    id("io.spring.dependency-management") version "1.1.4"
}

allprojects {
    group = "com.itinfo"
    version = Versions.Project.OKESTRO

    repositories {
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    tasks.withType<KotlinCompile> {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java

        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

subprojects {

    apply(plugin="org.jetbrains.kotlin.jvm")
    apply(plugin="org.jetbrains.kotlin.plugin.spring")
    apply(plugin="org.jetbrains.kotlin.plugin.jpa")
    apply(plugin="org.springframework.boot")
    apply(plugin="io.spring.dependency-management")

    tasks.withType<JavaCompile> {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
        options.encoding = "UTF-8"
        options.isIncremental = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configurations {
        all {
            exclude("org.springframework.boot", "spring-boot-starter-logging")
        }
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }


}

project("common") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

project("util") {

}

project(":license-common") {
    tasks.processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

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
}
project(":license-enc") {
    dependencies {
        compileOnly(project(":common"))
        compileOnly(project(":license-common"))
        compileOnly(Dependencies.kotlinStdlib)
        compileOnly(Dependencies.log4j)
        compileOnly(Dependencies.gson)

        testImplementation(project(":common"))
        testImplementation(Dependencies.log4j)
        testImplementation(Dependencies.junit)
        testImplementation(Dependencies.hamcrest)
    }
}
project(":license-dec") {
    dependencies {
        compileOnly(project(":common"))
        compileOnly(project(":license-common"))
        compileOnly(Dependencies.kotlinStdlib)
        compileOnly(Dependencies.log4j)
        compileOnly(Dependencies.gson)

        testImplementation(project(":common"))
        testImplementation(Dependencies.log4j)
        testImplementation(Dependencies.junit)
        testImplementation(Dependencies.hamcrest)
    }
}

project(":license-validate") {
    dependencies {
        compileOnly(project(":common"))
        compileOnly(project(":license-common"))
        compileOnly(project(":license-enc"))
        compileOnly(Dependencies.kotlinStdlib)
        compileOnly(Dependencies.log4j)
        compileOnly(Dependencies.gson)
    }
}
