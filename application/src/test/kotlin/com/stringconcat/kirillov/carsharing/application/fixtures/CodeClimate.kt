package com.stringconcat.kirillov.carsharing.application.fixtures

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

@Suppress("HasPlatformType", "PropertyName", "unused")
@AnalyzeClasses(packages = ["com.stringconcat.kirillov"])
class CodeClimate {
    @ArchTest
    val `project shouldn't contains cyclic dependencies between packages` =
        slices()
            .matching("com.stringconcat.kirillov.(**)")
            .should()
            .beFreeOfCycles()
}