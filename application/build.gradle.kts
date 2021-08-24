dependencies {
    implementation(project(":commons:ddd-types"))
    implementation(project(":commons:value-objects"))
    implementation(project(":maintenance:domain"))
    implementation(project(":maintenance:usecase"))
    implementation(project(":maintenance:persistence"))
    implementation(project(":purchasing-department:domain"))
    implementation(project(":purchasing-department:usecase"))
    implementation(project(":purchasing-department:persistence"))
    implementation(project(":ride:domain"))
    implementation(project(":ride:usecase"))
    implementation(project(":ride:persistence"))
    implementation(project(":customer:domain"))
    implementation(project(":customer:usecase"))
    implementation(project(":customer:persistence"))
    implementation(project(":integration:acquirer"))
    implementation(Libs.arrowCore)

    testImplementation(testFixtures(project(":commons:value-objects")))
    testImplementation(testFixtures(project(":maintenance:domain")))
    testImplementation(testFixtures(project(":purchasing-department:domain")))
    testImplementation(testFixtures(project(":purchasing-department:usecase")))
    testImplementation(testFixtures(project(":ride:domain")))
    testImplementation(testFixtures(project(":ride:usecase")))
    testImplementation(testFixtures(project(":customer:domain")))
    testImplementation(testFixtures(project(":integration:acquirer")))
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.junit5Params)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)
    testImplementation(Libs.archunitJunit5)
}