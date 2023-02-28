import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    val tomcatEmbedded = listOf(
        "org.apache.tomcat.embed:tomcat-embed-core:${Versions.tomcatEmbedded}",
        "org.apache.tomcat.embed:tomcat-embed-logging-juli:${Versions.tomcatEmbedded}",
        "org.apache.tomcat.embed:tomcat-embed-jasper:${Versions.tomcatEmbedded}"
    )
    val spring = listOf(
        "org.springframework:spring-aop:${Versions.spring}",
        "org.springframework:spring-beans:${Versions.spring}",
        "org.springframework:spring-context:${Versions.spring}",
        "org.springframework:spring-core:${Versions.spring}",
        "org.springframework:spring-expression:${Versions.spring}",
        "org.springframework:spring-jdbc:${Versions.spring}",
        "org.springframework:spring-messaging:${Versions.spring}",
        "org.springframework:spring-tx:${Versions.spring}",
        "org.springframework:spring-web:${Versions.spring}",
        "org.springframework:spring-webmvc:${Versions.spring}",
        "org.springframework:spring-websocket:${Versions.spring}",
    )
    val springTest = "org.springframework:spring-test:${Versions.spring}"
    val springSecurity = listOf(
        "org.springframework.security:spring-security-acl:${Versions.springSecurity}",
        "org.springframework.security:spring-security-config:${Versions.springSecurity}",
        "org.springframework.security:spring-security-core:${Versions.springSecurity}",
        "org.springframework.security:spring-security-taglibs:${Versions.springSecurity}",
        "org.springframework.security:spring-security-web:${Versions.springSecurity}",
    )
    var ovirt = listOf(
        "org.ovirt.engine.api:sdk:${Versions.ovirt}",
    )
    val qemu = listOf(
        "org.anarres.qemu:qemu-examples:${Versions.qemu}",
        "org.anarres.qemu:qemu-exec:${Versions.qemu}",
        "org.anarres.qemu:qemu-image:${Versions.qemu}",
        "org.anarres.qemu:qemu-qapi:${Versions.qemu}",
    )
    val tiles = listOf(
        "org.apache.tiles:tiles-api:${Versions.tiles}",
        "org.apache.tiles:tiles-core:${Versions.tiles}",
        "org.apache.tiles:tiles-jsp:${Versions.tiles}",
        "org.apache.tiles:tiles-servlet:${Versions.tiles}",
        "org.apache.tiles:tiles-template:${Versions.tiles}",
    )
    val mybatis = listOf(
        "org.mybatis:mybatis:${Versions.mybatis}",
        "org.mybatis:mybatis-spring:${Versions.mybatisSpring}"
    )
    val log4j = listOf(
        "log4j:log4j:${Versions.log4j}",
        "org.apache.logging.log4j:log4j-api:${Versions.log4jApache}",
        "org.apache.logging.log4j:log4j-core:${Versions.log4jApache}",
        "org.apache.logging.log4j:log4j-slf4j-impl:${Versions.log4jApache}",
    )
    val webjars = listOf(
        "org.webjars:jquery:${Versions.jquery}",
        "org.webjars:bootstrap:${Versions.bootstrap}",
    )
    val jdbc = listOf(
        "com.h2database:h2:${Versions.h2}",
        "org.postgresql:postgresql:${Versions.postgresql}",
        "commons-dbcp:commons-dbcp:${Versions.commonsDbcp}",
    )
    val commons = listOf(
        "commons-configuration:commons-configuration:${Versions.commonsConf}",
        "commons-fileupload:commons-fileupload:${Versions.commonsFileUpload}",
        "org.apache.commons:commons-lang3:3.4",
    )
    val jasypt = listOf(
        "org.jasypt:jasypt:${Versions.jasypt}",
        "org.jasypt:jasypt-spring3:${Versions.jasypt}",
    )
    val gson = "com.google.code.gson:gson:${Versions.gson}"
    val jsonSimple = "com.googlecode.json-simple:json-simple:${Versions.jsonSimple}"
    val javaxServletJstl = "javax.servlet:jstl:${Versions.javaxServletJstl}"
    val javaxInject = "javax.inject:javax.inject:1"
    val aspectj = listOf(
        "org.aspectj:aspectjweaver:${Versions.aspectj}",
        "org.aspectj:aspectjrt:${Versions.aspectjrt}",
    )
    val cglib = listOf(
        "cglib:cglib-nodep:${Versions.cglib}",
    )
    val kotlinStdlib = listOf(
        "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}",
        "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}",
        "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}",
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}",
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}",
        "org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:${Versions.kotlin}",
        "org.jetbrains.kotlin:kotlin-sam-with-receiver:${Versions.kotlin}",
    )
    val junit = listOf(
        "junit:junit:${Versions.junit}"
    )
    val hamcrest = listOf(
        "org.hamcrest:hamcrest-core:${Versions.hamcrest}",
    )
}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.tomcat(list: List<String>) {
    list.forEach { dependency ->
        add("tomcat", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}