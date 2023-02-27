import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    val spring = listOf(
        ""
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

    var ovirt = listOf(
        "org.ovirt.engine.api:sdk:${Versions.ovirt}",
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