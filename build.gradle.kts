import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_KOTLIN
import org.ajoberstar.grgit.Branch
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
    id(Plugins.grGit) version Versions.grGit
    id(Plugins.gradleVersions) version Versions.gradleVersions
    id(Plugins.detekt) version Versions.detekt
    jacoco
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

subprojects {
    apply {
        plugin("kotlin")
        plugin(Plugins.gradleVersions)
        plugin(Plugins.detekt)
        plugin("jacoco")
    }

    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation(Libs.kotlinStdlib)
        implementation(Libs.kotlinStdlibJdk8)
        implementation(Libs.kotlinReflect)

        testImplementation(Libs.junit5Api)
        testImplementation(Libs.junit5Jupiter)
    }

    val checkNonReleaseVersion = project.properties["checkNonReleaseVersion"] == "true"

    if (checkNonReleaseVersion || grgit.branch.current().isMainBranch()) {
        configurations.all {
            resolutionStrategy {
                eachDependency {
                    if (requested.version.isNonReleaseVersion()) {
                        throw GradleException("SNAPSHOT or RC dependency found: ${requested.name} ${requested.version}")
                    }
                }
            }
        }
    }

    detekt {
        allRules = false
        buildUponDefaultConfig = true
        config = files(rootDir.resolve("detekt/detekt-config.yml"))
        input = files(
            DEFAULT_SRC_DIR_KOTLIN,
            "src/test/kotlin"
        )

        reports {
            html.enabled = true
        }

        dependencies {
            detektPlugins("${Plugins.detektFormatting}:${Versions.detekt}")
        }
    }

    jacoco {
        toolVersion = "0.8.7"
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
        }

        val jacocoTestReport = named<JacocoReport>("jacocoTestReport")
        val jacocoTestCoverageVerification = named<JacocoCoverageVerification>("jacocoTestCoverageVerification")

        test {
            finalizedBy(jacocoTestReport)
        }

        jacocoTestReport {
            dependsOn(test)
            finalizedBy(jacocoTestCoverageVerification)
        }

        jacocoTestCoverageVerification {
            dependsOn(jacocoTestReport)

            violationRules {
                rule {
                    limit {
                        minimum = "0.8".toBigDecimal()
                    }
                }
            }
        }
    }
}

tasks.register<Copy>("installGitHooks") {
    from("$projectDir/git-hooks")
    into("$projectDir/.git/hooks")
}

tasks.register<Delete>("removeGitHooks") {
    delete(
        fileTree("$projectDir/.git/hooks").matching {
            include("**/*")
            exclude("**/*.sample")
        }
    )
}

fun Branch.isMainBranch(): Boolean = name?.toLowerCase() == "main"

fun String?.isNonReleaseVersion(): Boolean =
    this?.let { version ->
        version.contains("RC", ignoreCase = true) || version.contains("SNAPSHOT", ignoreCase = true)
    } ?: false