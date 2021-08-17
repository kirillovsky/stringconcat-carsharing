dependencies {
    implementation(project(":commons:ddd-types"))
    implementation(project(":commons:value-objects"))
    implementation(project(":maintenance:domain"))
    implementation(project(":maintenance:usecase"))
    implementation(project(":purchasing-department:domain"))
    implementation(project(":purchasing-department:usecase"))
    implementation(Libs.arrowCore)

    testImplementation(testFixtures(project(":commons:value-objects")))
    testImplementation(testFixtures(project(":maintenance:domain")))
    testImplementation(testFixtures(project(":purchasing-department:domain")))
    testImplementation(testFixtures(project(":purchasing-department:usecase")))
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)
}