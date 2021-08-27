dependencies {
    implementation(project(":commons:ddd-types"))
    implementation(project(":commons:value-objects"))
    implementation(project(":ride:domain"))
    implementation(project(":ride:usecase"))
    implementation(Libs.arrowCore)

    testImplementation(testFixtures(project(":commons:value-objects")))
    testImplementation(testFixtures(project(":ride:domain")))
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)
}