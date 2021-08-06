plugins {
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":commons:ddd-types"))
    implementation(project(":commons:value-objects"))
    implementation(Libs.arrowCore)

    testImplementation(testFixtures(project(":commons:value-objects")))
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)

    testFixturesImplementation(testFixtures(project(":commons:ddd-types")))
    testFixturesImplementation(testFixtures(project(":commons:value-objects")))
    testFixturesImplementation(Libs.arrowCore)
    testFixturesImplementation(Libs.faker)
}