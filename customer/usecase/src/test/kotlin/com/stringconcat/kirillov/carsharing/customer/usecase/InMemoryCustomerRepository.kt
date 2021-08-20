package com.stringconcat.kirillov.carsharing.customer.usecase

import com.stringconcat.kirillov.carsharing.customer.domain.Customer
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerId
import com.stringconcat.kirillov.carsharing.customer.domain.FullName
import java.time.LocalDate

class InMemoryCustomerRepository : CustomerPersister, CustomerExtractor, HashMap<CustomerId, Customer>() {
    override fun save(customer: Customer) {
        this[customer.id] = customer
    }

    override fun getBy(fullName: FullName, birthDate: LocalDate): Customer? =
        values.firstOrNull { it.fullName == fullName && it.birthDate == birthDate }
}