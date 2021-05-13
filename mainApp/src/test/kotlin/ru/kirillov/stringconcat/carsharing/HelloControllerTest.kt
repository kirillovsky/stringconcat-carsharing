package ru.kirillov.stringconcat.carsharing

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class HelloControllerTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `should return json on hello-endpoint`() {
        webTestClient.get()
            .uri("/")
            .exchange()
            .expectBody()
            .jsonPath("$.message")
            .isEqualTo("Hello")
    }
}