dependencies {
    implementation(project(":customer:domain"))

    testImplementation(testFixtures(project(":customer:domain")))
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)
}