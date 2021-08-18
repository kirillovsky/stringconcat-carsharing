package com.stringconcat.kirillov.carsharing.customer

import java.time.LocalDate

interface CustomerExtractor {
    fun getBy(fullName: FullName, birthDate: LocalDate): Customer?
}
