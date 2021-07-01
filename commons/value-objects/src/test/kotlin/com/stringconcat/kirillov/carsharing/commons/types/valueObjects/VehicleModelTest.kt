package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.VehicleModel.EmptyOrBlankMaker
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.VehicleModel.EmptyOrBlankName
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class VehicleModelTest {
    @Test
    fun `vehicle mode should contains model and maker`() {
        val expectedName = "Camry"
        val expectedMaker = "Toyota"
        val model = VehicleModel.from(expectedName, expectedMaker)

        model.shouldBeSuccess {
            it?.maker shouldBe expectedMaker
            it?.name shouldBe expectedName
        }
    }

    @Test
    fun `shouldn't create vehicle with blank or empty name`() {
        val emptyNameModel = VehicleModel.from(name = "", maker = "Toyota")
        emptyNameModel.shouldBeFailure {
            it shouldBe EmptyOrBlankName
        }

        val blankNameModel = VehicleModel.from(name = "  ", maker = "Toyota")
        blankNameModel.shouldBeFailure {
            it shouldBe EmptyOrBlankName
        }
    }

    @Test
    fun `shouldn't create vehicle with blank or empty maker`() {
        val emptyMakerModel = VehicleModel.from(name = "Camry", maker = "")
        emptyMakerModel.shouldBeFailure {
            it shouldBe EmptyOrBlankMaker
        }

        val blankMakerModel = VehicleModel.from(name = "Camry", maker = "   ")
        blankMakerModel.shouldBeFailure {
            it shouldBe EmptyOrBlankMaker
        }
    }
}