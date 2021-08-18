package com.stringconcat.kirillov.carsharing.integration.gis.gmp

import com.stringconcat.kirillov.carsharing.customer.CustomerActuallyExists
import com.stringconcat.kirillov.carsharing.customer.FullName
import java.time.LocalDate

object CustomerAlwaysExistsInGisGmp : CustomerActuallyExists {
    override fun check(fullName: FullName, birthDate: LocalDate): Boolean = true
}