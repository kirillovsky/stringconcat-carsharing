package com.stringconcat.kirillov.carsharing.customer.persistence

import com.stringconcat.kirillov.carsharing.customer.domain.Customer
import com.stringconcat.kirillov.carsharing.customer.domain.FullName
import com.stringconcat.kirillov.carsharing.customer.usecase.CustomerExtractor
import java.time.LocalDate

class StubCustomerExtractor : CustomerExtractor {
    override fun getBy(fullName: FullName, birthDate: LocalDate): Customer? = null
}