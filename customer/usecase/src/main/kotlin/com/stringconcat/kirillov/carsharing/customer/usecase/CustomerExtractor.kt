package com.stringconcat.kirillov.carsharing.customer.usecase

import com.stringconcat.kirillov.carsharing.customer.domain.Customer
import com.stringconcat.kirillov.carsharing.customer.domain.FullName
import java.time.LocalDate

interface CustomerExtractor {
    fun getBy(fullName: FullName, birthDate: LocalDate): Customer?
}
