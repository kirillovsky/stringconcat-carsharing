package com.stringconcat.kirillov.carsharing.application.fixtures

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaPackage
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.metrics.ArchitectureMetrics
import com.tngtech.archunit.library.metrics.ComponentDependencyMetrics
import com.tngtech.archunit.library.metrics.MetricsComponents
import io.kotest.matchers.doubles.shouldBeLessThan

private const val BASE_PACKAGE = "com.stringconcat.kirillov.carsharing"
private const val RIDE_PACKAGE = "$BASE_PACKAGE.ride"
private const val PURCHASING_DEPARTMENT_PACKAGE = "$BASE_PACKAGE.purchasingDepartment"
private const val MAINTENANCE_PACKAGE = "$BASE_PACKAGE.maintenance"
private const val CUSTOMER_PACKAGE = "$BASE_PACKAGE.customer"
private const val DISTANCE_FROM_MAIN_SEQUENCE_DELTA = 0.3

@AnalyzeClasses(packages = [BASE_PACKAGE])
class UncleBobMetricsReport {
    private val contextsBasePackages = listOf(
        RIDE_PACKAGE,
        PURCHASING_DEPARTMENT_PACKAGE,
        MAINTENANCE_PACKAGE,
        CUSTOMER_PACKAGE
    )

    @ArchTest
    fun `report uncle bob metrics by contexts components`(importedClasses: JavaClasses) {
        contextsBasePackages.forEach { contextBasePackage ->
            val metrics = importedClasses
                .projectSubpackages(contextBasePackage)
                .toUncleBobMetrics()

            listOf("domain", "usecase", "persistence")
                .map { "$contextBasePackage.$it" }
                .forEach(metrics::printUncleBobMetricsReport)
        }
    }

    @ArchTest
    fun `overall report uncle bob metrics for contexts in application`(importedClasses: JavaClasses) {
        val metrics = importedClasses
            .projectSubpackages(BASE_PACKAGE)
            .toUncleBobMetrics()

        contextsBasePackages.forEach(metrics::printUncleBobMetricsReport)
    }

    @ArchTest
    fun `ride persistence should near main sequence`(importedClasses: JavaClasses) {
        val metrics = importedClasses
            .projectSubpackages(RIDE_PACKAGE)
            .toUncleBobMetrics()

        metrics.getNormalizedDistanceFromMainSequence("$RIDE_PACKAGE.persistence")
            .shouldBeLessThan(DISTANCE_FROM_MAIN_SEQUENCE_DELTA)
    }

    @ArchTest
    fun `maintenance persistence should near main sequence`(importedClasses: JavaClasses) {
        val metrics = importedClasses
            .projectSubpackages(MAINTENANCE_PACKAGE)
            .toUncleBobMetrics()

        metrics.getNormalizedDistanceFromMainSequence("$MAINTENANCE_PACKAGE.persistence")
            .shouldBeLessThan(DISTANCE_FROM_MAIN_SEQUENCE_DELTA)
    }

    @ArchTest
    fun `customer persistence distance should near main sequence`(importedClasses: JavaClasses) {
        val metrics = importedClasses
            .projectSubpackages(CUSTOMER_PACKAGE)
            .toUncleBobMetrics()

        metrics.getNormalizedDistanceFromMainSequence("$CUSTOMER_PACKAGE.persistence")
            .shouldBeLessThan(DISTANCE_FROM_MAIN_SEQUENCE_DELTA)
    }

    @ArchTest
    fun `purchasing-department persistence should near main sequence`(importedClasses: JavaClasses) {
        val metrics = importedClasses.projectSubpackages(PURCHASING_DEPARTMENT_PACKAGE)
            .toUncleBobMetrics()

        metrics.getNormalizedDistanceFromMainSequence("$PURCHASING_DEPARTMENT_PACKAGE.persistence")
            .shouldBeLessThan(DISTANCE_FROM_MAIN_SEQUENCE_DELTA)
    }
}

private fun ComponentDependencyMetrics.printUncleBobMetricsReport(packageName: String) {
    println(
        """----------------$packageName----------------------------
            |Ca (incoming): ${getAfferentCoupling(packageName)}
            |Ce (outgoing): ${getEfferentCoupling(packageName)}
            |I (instability): ${getInstability(packageName)}
            |A (abstractness): ${getAbstractness(packageName)}
            |D (normalized distance): ${getNormalizedDistanceFromMainSequence(packageName)}
            """.trimMargin()
    )
}

private fun JavaClasses.projectSubpackages(projectBasePackage: String): Set<JavaPackage> =
    getPackage(projectBasePackage).subpackages

private fun Collection<JavaPackage>.toUncleBobMetrics(): ComponentDependencyMetrics =
    ArchitectureMetrics.componentDependencyMetrics(MetricsComponents.fromPackages(this))