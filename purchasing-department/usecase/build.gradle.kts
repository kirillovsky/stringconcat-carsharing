plugins {
    id(Plugins.javaTestFixtures)
    idea
}

dependencies {
    implementation(project(":commons:ddd-types"))
    implementation(project(":commons:value-objects"))
    implementation(project(":purchasing-department:domain"))
    implementation(Libs.arrowCore)

    testImplementation(testFixtures(project(":commons:value-objects")))
    testImplementation(testFixtures(project(":purchasing-department:domain")))
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)
    testImplementation(Libs.junit5Params)

    implementation(project(":purchasing-department:domain"))
}