package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVinError.IllegalCharacterContains
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVinError.InvalidCodeLength
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VinTest {
    @Test
    fun `vin should contains code`() {
        val expectedVinCode = "KMHJU81VCBU266113"
        val actualVin = Vin.from(expectedVinCode)

        actualVin.shouldBeRight().code shouldBe expectedVinCode
    }

    @Test
    fun `vin creation should be code case agnostic`() {
        val vinCode = "KMHJU81VCBU266113"
        val actualVin = Vin.from(vinCode)
        val expectedVin = Vin.from(vinCode.lowercase())

        actualVin.shouldBeRight()
        expectedVin.shouldBeRight()
        actualVin.value shouldBe expectedVin.value
    }

    @Test
    fun `vin should contains only 17 symbols`() {
        val invalidVin = Vin.from("KMHJ")

        invalidVin shouldBeLeft InvalidCodeLength
    }

    @Test
    fun `vin should contains only digits or latin letters`() {
        val invalidVin = Vin.from("!@#$%^&*()_+-,./\\")

        invalidVin shouldBeLeft IllegalCharacterContains
    }

    @ParameterizedTest(name = "vin shouldn't contains special latin letter - '{0}'")
    @ValueSource(chars = ['I', 'i', 'Q', 'q', 'O', 'o'])
    fun `vin shouldn't contains special latin letter`(specialLetter: Char) {
        val invalidVin = Vin.from("KMHJU81VC${specialLetter}U266113")

        invalidVin shouldBeLeft IllegalCharacterContains
    }
}