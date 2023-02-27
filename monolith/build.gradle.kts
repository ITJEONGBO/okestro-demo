plugins {
    kotlin("jvm")
    application
}

group = "com.itinfo"
version = "0.0.1"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.ovirt)
}

application {
    mainClass.set("MainKt")
}
