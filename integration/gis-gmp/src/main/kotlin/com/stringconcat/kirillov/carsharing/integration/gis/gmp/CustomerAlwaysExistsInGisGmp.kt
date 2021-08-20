package com.stringconcat.kirillov.carsharing.integration.gis.gmp

import com.stringconcat.kirillov.carsharing.customer.domain.CustomerActuallyExists
import com.stringconcat.kirillov.carsharing.customer.domain.FullName
import java.time.LocalDate

object CustomerAlwaysExistsInGisGmp : CustomerActuallyExists {
    override fun check(fullName: FullName, birthDate: LocalDate): Boolean = true
}