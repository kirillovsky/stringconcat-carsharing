import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlin
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
            }
        }

        withType<Test> {
            useJUnitPlatform()
            maxParallelForks = 10
        }
    }
}