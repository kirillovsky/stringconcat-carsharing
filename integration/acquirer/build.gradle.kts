plugins {
    id(Plugins.javaTestFixtures)
}

dependencies {
    implementation(project(":ride:domain"))
    implementation(project(":commons:value-objects"))

    testImplementation(testFixtures(project(":ride:domain")))
    testImplementation(testFixtures(project(":commons:value-objects")))
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)

    testFixturesImplementation(project(":ride:domain"))
    testFixturesImplementation(project(":commons:value-objects"))
}