package com.stringconcat.kirillov.carsharing.customer

import java.time.LocalDate

fun interface CustomerAlreadyRegistered {
    fun check(customerFullName: FullName, customerBirthDate: LocalDate): Boolean
}
