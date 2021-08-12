plugins {
    id(Plugins.javaTestFixtures)
}

dependencies {
    implementation(project(":commons:ddd-types"))
    implementation(Libs.arrowCore)

    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)
    testImplementation(Libs.junit5Params)

    testFixturesImplementation(testFixtures(project(":commons:ddd-types")))
    testFixturesImplementation(Libs.arrowCore)
    testFixturesImplementation(Libs.faker)
}