package ru.kirillov.stringconcat.carsharing

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping
    fun hello(): String = """{"message": "Hello"}"""
}