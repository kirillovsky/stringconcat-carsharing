package com.stringconcat.kirillov.carsharing.customer.domain

import com.stringconcat.kirillov.carsharing.customer.domain.CreateFullNameError.InvalidFirstName
import com.stringconcat.kirillov.carsharing.customer.domain.CreateFullNameError.InvalidMiddleName
import com.stringconcat.kirillov.carsharing.customer.domain.CreateFullNameError.InvalidSecondName
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class FullNameTest {
    private val correctFirstName = "ВАСИЛИЙ"
    private val correctMiddleName = "ВАСИЛЬЕВИЧ"
    private val correctSecondName = "ПУПКИН"

    @Test
    fun `fullName should contains trimmed firstName, middleName and secondName in uppercase`() {
        val fullName = FullName.from(
            firstName = "  ${correctFirstName.lowercase()} ",
            middleName = "   ${correctMiddleName.lowercase()} ",
            secondName = "  ${correctSecondName.lowercase()} "
        )

        fullName.shouldBeRight().let {
            it.firstName shouldBe correctFirstName
            it.middleName shouldBe correctMiddleName
            it.secondName shouldBe correctSecondName
        }
    }

    @ParameterizedTest(name = "fullName should contains only russian letters in middleName - {0}")
    @ValueSource(strings = ["  ", "", "ВАС2ЛЬЕВИЧ", "ВАСYЛЬЕВИЧ", "!ВАСИЛЬЕВИЧ", "BА'СИЛЬЕВИЧ", "ВАС-ИЛЬЕВИЧ"])
    fun `fullName should contains only russian letters in middleName`(middleName: String) {
        val fullName = FullName.from(correctFirstName, middleName, correctSecondName)

        fullName shouldBeLeft InvalidMiddleName
    }

    @Test
    fun `fullName can contains nullable middleName`() {
        val fullName = FullName.from(firstName = correctFirstName, secondName = correctSecondName)

        fullName.shouldBeRight().middleName shouldBe null
    }

    @ParameterizedTest(name = "fullName should contains only russian letters in firstName - {0}")
    @ValueSource(strings = ["  ", "", "ВАСИЛИ1", "FАСИЛИЙ", "!АСИЛИЙ", "BА'СИЛИЙ", "BА-СИЛИЙ"])
    fun `fullName should contains only russian letters in firstName`(firstName: String) {
        val fullName = FullName.from(firstName, secondName = correctSecondName)

        fullName shouldBeLeft InvalidFirstName
    }

    @ParameterizedTest(name = "fullName should contains only russian letters or hyphen in secondName - {0}")
    @ValueSource(strings = ["  ", "", "ПУПК1Н", "ПУDКИН", "!ПУПКИН", "ПУП,КИН"])
    fun `fullName should contains russian letters in secondName`(secondName: String) {
        val fullName = FullName.from(firstName = correctFirstName, secondName = secondName)

        fullName shouldBeLeft InvalidSecondName
    }

    @Test
    fun `fullName can contains hyphen in secondName`() {
        val expectedSecondName = "ПУПКИН-ИВАНОВ"
        val fullName = FullName.from(firstName = correctFirstName, secondName = expectedSecondName)

        fullName.shouldBeRight().secondName shouldBe expectedSecondName
    }
}