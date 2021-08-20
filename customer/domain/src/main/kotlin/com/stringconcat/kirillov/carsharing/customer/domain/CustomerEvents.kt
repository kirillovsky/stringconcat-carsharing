package com.stringconcat.kirillov.carsharing.customer.domain

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent

data class CustomerRegistered(val customerId: CustomerId) : DomainEvent()
data class CustomerRejected(val customerId: CustomerId) : DomainEvent()