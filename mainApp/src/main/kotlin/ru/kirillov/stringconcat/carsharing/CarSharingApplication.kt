package ru.kirillov.stringconcat.carsharing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CarSharingApplication

fun main(args: Array<String>) {
	runApplication<CarSharingApplication>(*args)
}