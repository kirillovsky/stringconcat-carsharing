package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import kotlin.random.Random.Default.nextLong

fun purchasingVehicleId() = PurchasingVehicleId(value = nextLong())