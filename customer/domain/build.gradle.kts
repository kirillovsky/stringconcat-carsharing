dependencies {
    implementation(project(":commons:ddd-types"))

    implementation(Libs.arrowCore)

    testImplementation(Libs.junit5Params)
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotestArrow)
    testImplementation(Libs.kotlinScriptRuntime)
    testImplementation(Libs.faker)
}