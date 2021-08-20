package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent

data class CustomerRegistered(val customerId: CustomerId) : DomainEvent()
data class CustomerRejected(val customerId: CustomerId) : DomainEvent()