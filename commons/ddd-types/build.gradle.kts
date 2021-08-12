plugins {
    id(Plugins.javaTestFixtures)
}

dependencies {
    testImplementation(Libs.kotestJunit5Runner)
    testImplementation(Libs.kotlinScriptRuntime)
}