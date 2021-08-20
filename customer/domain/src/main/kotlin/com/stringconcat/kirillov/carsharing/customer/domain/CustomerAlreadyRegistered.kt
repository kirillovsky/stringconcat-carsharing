package com.stringconcat.kirillov.carsharing.customer.domain

import java.time.LocalDate

fun interface CustomerAlreadyRegistered {
    fun check(customerFullName: FullName, customerBirthDate: LocalDate): Boolean
}
