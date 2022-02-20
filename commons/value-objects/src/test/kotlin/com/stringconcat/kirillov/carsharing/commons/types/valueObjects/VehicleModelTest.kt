package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVehicleModelError.EmptyOrBlankMaker
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVehicleModelError.EmptyOrBlankName
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class VehicleModelTest {
    @Test
    fun `vehicle mode should contains model and maker`() {
        val expectedName = "Camry"
        val expectedMaker = "Toyota"
        val model = VehicleModel.from(expectedName, expectedMaker).shouldBeRight()

        model.let {
            it.maker shouldBe expectedMaker
            it.name shouldBe expectedName
        }
    }

    @Test
    fun `shouldn't create vehicle with blank or empty name`() {
        val emptyNameModel = VehicleModel.from(name = "", maker = "Toyota")
        emptyNameModel shouldBeLeft EmptyOrBlankName

        val blankNameModel = VehicleModel.from(name = "  ", maker = "Toyota")
        blankNameModel shouldBeLeft EmptyOrBlankName
    }

    @Test
    fun `shouldn't create vehicle with blank or empty maker`() {
        val emptyMakerModel = VehicleModel.from(name = "Camry", maker = "")
        emptyMakerModel shouldBeLeft EmptyOrBlankMaker

        val blankMakerModel = VehicleModel.from(name = "Camry", maker = "   ")
        blankMakerModel shouldBeLeft EmptyOrBlankMaker
    }
}