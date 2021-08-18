package com.stringconcat.kirillov.carsharing.customer

import java.time.LocalDate

class FakeCustomerExtractor(vararg customers: Customer) : CustomerExtractor {
    private val customersList = customers.toList()

    override fun getBy(fullName: FullName, birthDate: LocalDate): Customer? =
        customersList.firstOrNull { it.fullName == fullName && it.birthDate == birthDate }
}
