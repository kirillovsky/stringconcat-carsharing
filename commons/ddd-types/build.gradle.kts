plugins {
    id("java-test-fixtures")
}

dependencies {
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotlinScriptRuntime)
}