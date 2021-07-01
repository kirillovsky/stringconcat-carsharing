package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin.IllegalCharacterContains
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin.InvalidCodeLength
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VinTest {
    @Test
    fun `vin should contains code`() {
        val expectedVinCode = "KMHJU81VCBU266113"
        val actualVin = Vin.from(expectedVinCode)

        actualVin.shouldBeSuccess {
            it?.code shouldBe expectedVinCode
        }
    }

    @Test
    fun `vin creation should be code case agnostic`() {
        val vinCode = "KMHJU81VCBU266113"
        val actualVin = Vin.from(vinCode)
        val expectedVin = Vin.from(vinCode.lowercase())

        actualVin.shouldBeSuccess()
        expectedVin.shouldBeSuccess()
        actualVin.getOrThrow() shouldBe expectedVin.getOrThrow()
    }

    @Test
    fun `vin should contains only 17 symbols`() {
        val invalidVin = Vin.from("KMHJ")

        invalidVin.shouldBeFailure {
            it shouldBe InvalidCodeLength
        }
    }

    @Test
    fun `vin should contains only digits or latin letters`() {
        val invalidVin = Vin.from("!@#$%^&*()_+-,./\\")

        invalidVin.shouldBeFailure {
            it shouldBe IllegalCharacterContains
        }
    }

    @ParameterizedTest(name = "vin shouldn't contains special latin letter - `{0}`")
    @ValueSource(chars = ['I', 'i', 'Q', 'q', 'O', 'o'])
    fun `vin shouldn't contains special latin letter`(specialLetter: Char) {
        val invalidVin = Vin.from("KMHJU81VC${specialLetter}U266113")

        invalidVin.shouldBeFailure {
            it shouldBe IllegalCharacterContains
        }
    }
}