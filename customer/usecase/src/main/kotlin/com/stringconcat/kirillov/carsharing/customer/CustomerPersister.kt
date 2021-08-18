package com.stringconcat.kirillov.carsharing.customer

interface CustomerPersister {
    fun save(customer: Customer)
}
