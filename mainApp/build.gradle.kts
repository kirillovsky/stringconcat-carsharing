plugins {
    id(Plugins.springBoot) version Versions.springBoot
    id(Plugins.springDependencyManagement) version Versions.springDependencyManagement
    kotlin(Plugins.kotlinSpring) version Versions.kotlin
}

dependencies {
    implementation(Libs.springBootWebfluxStarter)

    implementation(Libs.jacksonKotlinModule)
    implementation(Libs.kotlinReflect)

    testImplementation(Libs.springBootTestStarter) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks {
    withType<JacocoCoverageVerification> {
        afterEvaluate {
            excludeFilesFromDirectory(classDirectories, listOf("**/CarSharingApplication*.*"))
        }
    }

    withType<JacocoReport> {
        afterEvaluate {
            excludeFilesFromDirectory(classDirectories, listOf("**/CarSharingApplication*.*"))
        }
    }
}