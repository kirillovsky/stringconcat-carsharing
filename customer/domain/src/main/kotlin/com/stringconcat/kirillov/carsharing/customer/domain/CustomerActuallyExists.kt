package com.stringconcat.kirillov.carsharing.customer.domain

import java.time.LocalDate

fun interface CustomerActuallyExists {
    fun check(fullName: FullName, birthDate: LocalDate): Boolean
}
