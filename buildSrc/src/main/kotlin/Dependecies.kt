object Versions {
    const val kotlin = "1.5.0"
    const val springBoot = "2.4.5"
    const val springDependencyManagement = "1.0.11.RELEASE"
    const val grGit = "4.1.0"

    const val gradleVersions = "0.38.0"
    const val detekt = "1.16.0"
}

object Plugins {
    const val springBoot = "org.springframework.boot"
    const val springDependencyManagement = "io.spring.dependency-management"
    const val kotlinSpring = "plugin.spring"
    const val grGit = "org.ajoberstar.grgit"
    const val gradleVersions = "com.github.ben-manes.versions"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
}

object Libs {
    const val kotlinStdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"
    const val jacksonKotlinModule = "com.fasterxml.jackson.module:jackson-module-kotlin"

    const val springBootWebfluxStarter = "org.springframework.boot:spring-boot-starter-webflux"
    const val springBootTestStarter = "org.springframework.boot:spring-boot-starter-test"
}