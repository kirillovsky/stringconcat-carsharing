package com.stringconcat.kirillov.carsharing.application.fixtures

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures

private const val BASE_PACKAGE = "com.stringconcat.kirillov.carsharing"
private const val COMMONS_PACKAGE = "com.stringconcat.kirillov.carsharing.commons"
private const val PURCHASING_DEPARTMENT_PACKAGE = "$BASE_PACKAGE.purchasingDepartment"
private const val CUSTOMER_PACKAGE = "$BASE_PACKAGE.customer"
private const val RIDE_PACKAGE = "$BASE_PACKAGE.ride"
private const val MAINTENANCE_PACKAGE = "$BASE_PACKAGE.maintenance"

@Suppress("unused", "PropertyName", "HasPlatformType")
@AnalyzeClasses(packages = [PURCHASING_DEPARTMENT_PACKAGE, CUSTOMER_PACKAGE, RIDE_PACKAGE, MAINTENANCE_PACKAGE])
class CleanArchitecture {
    @ArchTest
    val `onion architecture should be followed for purchasing-department` =
        Architectures.onionArchitecture()
            .domainModels("$PURCHASING_DEPARTMENT_PACKAGE.domain..")
            .domainServices("$PURCHASING_DEPARTMENT_PACKAGE.domain..")
            .applicationServices("$PURCHASING_DEPARTMENT_PACKAGE.usecase..")
            .adapter("persistence", "$PURCHASING_DEPARTMENT_PACKAGE.persistence..")

    @ArchTest
    val `puschasing-department business logic should depends only on approved packages` =
        ArchRuleDefinition.classes()
            .that()
            .resideInAPackage("$PURCHASING_DEPARTMENT_PACKAGE.domain..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "$PURCHASING_DEPARTMENT_PACKAGE.domain..",
                "$COMMONS_PACKAGE..",
                "kotlin..",
                "java..",
                "org.jetbrains.annotations..",
                "arrow.core.."
            )

    @ArchTest
    val `onion architecture should be followed for customer` =
        Architectures.onionArchitecture()
            .domainModels("$CUSTOMER_PACKAGE.domain..")
            .domainServices("$CUSTOMER_PACKAGE.domain..")
            .applicationServices("$CUSTOMER_PACKAGE.usecase..")
            .adapter("persistence", "$CUSTOMER_PACKAGE.persistence..")

    @ArchTest
    val `customer business logic should depends only on approved packages` =
        ArchRuleDefinition.classes()
            .that()
            .resideInAPackage("$CUSTOMER_PACKAGE.domain..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "$CUSTOMER_PACKAGE.domain..",
                "$COMMONS_PACKAGE..",
                "kotlin..",
                "java..",
                "org.jetbrains.annotations..",
                "arrow.core.."
            )

    @ArchTest
    val `onion architecture should be followed for ride` =
        Architectures.onionArchitecture()
            .domainModels("$RIDE_PACKAGE.domain..")
            .domainServices("$RIDE_PACKAGE.domain..")
            .applicationServices("$RIDE_PACKAGE.usecase..")
            .adapter("persistence", "$RIDE_PACKAGE.persistence..")

    @ArchTest
    val `ride business logic should depends only on approved packages` =
        ArchRuleDefinition.classes()
            .that()
            .resideInAPackage("$RIDE_PACKAGE.domain..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "$RIDE_PACKAGE.domain..",
                "$COMMONS_PACKAGE..",
                "kotlin..",
                "java..",
                "org.jetbrains.annotations..",
                "arrow.core.."
            )

    @ArchTest
    val `onion architecture should be followed for maintenance` =
        Architectures.onionArchitecture()
            .domainModels("$MAINTENANCE_PACKAGE.domain..")
            .domainServices("$MAINTENANCE_PACKAGE.domain..")
            .applicationServices("$MAINTENANCE_PACKAGE.usecase..")
            .adapter("persistence", "$MAINTENANCE_PACKAGE.persistence..")

    @ArchTest
    val `maintenance business logic should depends only on approved packages` =
        ArchRuleDefinition.classes()
            .that()
            .resideInAPackage("$MAINTENANCE_PACKAGE.domain..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "$MAINTENANCE_PACKAGE.domain..",
                "$COMMONS_PACKAGE..",
                "kotlin..",
                "java..",
                "org.jetbrains.annotations..",
                "arrow.core.."
            )
}