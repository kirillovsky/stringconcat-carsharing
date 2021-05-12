import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.ajoberstar.grgit.Branch
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    id(Plugins.grGit) version Versions.grGit
    id(Plugins.gradleVersions) version Versions.gradleVersions
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
        plugin(Plugins.gradleVersions)
    }

    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation(Libs.kotlinStdlibJdk8)
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

    tasks {
        val check = named<DefaultTask>("check")
        val dependencyUpdate = named<DependencyUpdatesTask>("dependencyUpdates")

        dependencyUpdate.configure {
            revision = "release"
            outputFormatter = "html"
            checkForGradleUpdate = true
            outputDir = "$buildDir/reports/dependencies"
            reportfileName = "suggested-updates"

            fun isNonStable(version: String): Boolean {
                val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
                val regex = "^[0-9,.v-]+(-r)?$".toRegex()
                val isStable = stableKeyword || regex.matches(version)
                return isStable.not()
            }

            rejectVersionIf {
                isNonStable(candidate.version) && !isNonStable(currentVersion)
            }
        }

        check {
            finalizedBy(dependencyUpdate)
        }


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