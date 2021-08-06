package com.stringconcat.kirillov.carsharing.commons.types.error

fun failOnBusinessError(error: BusinessError): Nothing = throw IllegalStateException(error.toString())