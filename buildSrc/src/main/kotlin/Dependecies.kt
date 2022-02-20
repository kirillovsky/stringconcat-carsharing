object Versions {
    const val kotlin = "1.6.10"
    const val springBoot = "2.6.3"
    const val springDependencyManagement = "1.0.11.RELEASE"
    const val grGit = "5.0.0"
    const val gradleVersions = "0.42.0"
    const val arrowCore = "1.0.1"
    const val faker = "1.0.2"

    const val junit5 = "5.8.2"
    const val detekt = "1.19.0"
    const val kotest = "5.1.0"
    const val kotestArrow = "1.2.3"
    const val archUnit = "0.22.0"
}

object Plugins {
    const val springBoot = "org.springframework.boot"
    const val springDependencyManagement = "io.spring.dependency-management"
    const val kotlinSpring = "plugin.spring"
    const val grGit = "org.ajoberstar.grgit"
    const val gradleVersions = "com.github.ben-manes.versions"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
    const val javaTestFixtures = "java-test-fixtures"
}

object Libs {
    const val kotlinStdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"
    const val kotlinScriptRuntime = "org.jetbrains.kotlin:kotlin-script-runtime"
    const val jacksonKotlinModule = "com.fasterxml.jackson.module:jackson-module-kotlin"
    const val arrowCore = "io.arrow-kt:arrow-core:${Versions.arrowCore}"

    const val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}"
    const val junit5Jupiter = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
    const val junit5Params = "org.junit.jupiter:junit-jupiter-params:${Versions.junit5}"
    const val springBootWebfluxStarter = "org.springframework.boot:spring-boot-starter-webflux"
    const val springBootTestStarter = "org.springframework.boot:spring-boot-starter-test"
    const val kotestJunit5Runner = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
    const val kotestArrow = "io.kotest.extensions:kotest-assertions-arrow:${Versions.kotestArrow}"
    const val faker = "com.github.javafaker:javafaker:${Versions.faker}"
    const val archunitJunit5 = "com.tngtech.archunit:archunit-junit5:${Versions.archUnit}"
}