package com.stringconcat.kirillov.carsharing.customer.usecase

import com.stringconcat.kirillov.carsharing.customer.domain.Customer

interface CustomerPersister {
    fun save(customer: Customer)
}
