package ru.kirillov.stringconcat.carsharing

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CarSharingApplicationTests {

    @Test
    fun contextLoads() {
        val a = 2

        assert(a == 2)
    }
}