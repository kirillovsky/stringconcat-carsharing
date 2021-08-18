package com.stringconcat.kirillov.carsharing.customer

import java.time.LocalDate

class ExtractedCustomerAlreadyExists(private val extractor: CustomerExtractor) : CustomerActuallyExists {
    override fun check(fullName: FullName, birthDate: LocalDate): Boolean {
        return extractor.getBy(fullName, birthDate) != null
    }
}