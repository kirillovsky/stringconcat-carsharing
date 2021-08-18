package com.stringconcat.kirillov.carsharing.customer

class FakeCustomerPersister : CustomerPersister, HashMap<CustomerId, Customer>() {
    override fun save(customer: Customer) {
        this[customer.id] = customer
    }
}