package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent

data class CustomerRegistered(val value: CustomerId) : DomainEvent()
data class CustomerRejected(val value: CustomerId) : DomainEvent()
data class CustomerVerified(val value: CustomerId) : DomainEvent()