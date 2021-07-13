package com.stringconcat.kirillov.carsharing.customer

import java.time.LocalDate

fun interface CustomerIsMaturedEnough {
    fun check(customerBirthDate: LocalDate): Boolean
}