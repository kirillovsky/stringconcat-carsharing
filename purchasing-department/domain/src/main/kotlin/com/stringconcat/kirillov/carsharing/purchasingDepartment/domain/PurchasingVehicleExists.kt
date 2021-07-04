package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin

object PurchasingVehicleExists {
    fun interface ByVin {
        fun check(vin: Vin): Boolean
    }

    fun interface ByRegistrationPlate {
        fun check(registrationPlate: RegistrationPlate): Boolean
    }
}