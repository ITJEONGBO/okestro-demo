import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
}

allprojects {
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
            jvmTarget = Versions.java
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
        options.encoding = "UTF-8"
        options.isIncremental = true
    }



    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

