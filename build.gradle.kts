import org.ajoberstar.grgit.Branch
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    id(Plugins.grGit) version Versions.grGit
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(Libs.kotlinStdlib)
        classpath(Libs.kotlinStdlibJdk8)
        classpath(Libs.kotlinReflect)
    }
}

repositories {
    mavenCentral()
}

allprojects {
    group = "ru.kirillov.stringconcat"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply {
        plugin("kotlin")
    }

    if (grgit.branch.current().isMainBranch()) {
        configurations.all {
            resolutionStrategy {
                eachDependency {
                    if (requested.version.isRcOrSnapshotVersion()) {
                        throw GradleException("SNAPSHOT or RC dependency found: ${requested.name} ${requested.version}")
                    }
                }
            }
        }
    }

    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation(Libs.kotlinStdlibJdk8)
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
                allWarningsAsErrors = true
            }
        }

        withType<Test> {
            useJUnitPlatform()
            maxParallelForks = 10
        }
    }
}

fun Branch.isMainBranch(): Boolean = name?.toLowerCase() == "main"

fun String?.isRcOrSnapshotVersion(): Boolean =
    this?.let { version ->
        version.contains("RC", ignoreCase = true) || version.contains("SNAPSHOT", ignoreCase = true)
    } ?: false

