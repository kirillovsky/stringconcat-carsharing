dependencies {
    implementation(project(":commons:ddd-types"))
    implementation(project(":commons:value-objects"))
    implementation(project(":customer:domain"))
    implementation(project(":customer:usecase"))
    implementation(Libs.arrowCore)

    testImplementation(testFixtures(project(":commons:value-objects")))
    testImplementation(testFixtures(project(":customer:domain")))
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)
    testImplementation(Libs.junit5Params)
}